package com.google.soul.ui.activity

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AlertDialog
import android.view.View
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback
import cn.jpush.im.android.api.model.UserInfo
import cn.jpush.im.api.BasicCallback
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.soul.R
import com.google.soul.base.BaseActivity
import com.google.soul.mvp.model.*
import com.google.soul.utils.AppUtil
import com.google.soul.utils.CustomToast
import com.google.soul.widget.InputDialog
import com.jph.takephoto.app.TakePhoto
import com.jph.takephoto.app.TakePhotoImpl
import com.jph.takephoto.model.CropOptions
import com.jph.takephoto.model.InvokeParam
import com.jph.takephoto.model.TContextWrap
import com.jph.takephoto.model.TResult
import com.jph.takephoto.permission.InvokeListener
import com.jph.takephoto.permission.PermissionManager
import com.jph.takephoto.permission.TakePhotoInvocationHandler
import kotlinx.android.synthetic.main.activity_user_info.*
import org.greenrobot.eventbus.EventBus
import org.json.JSONArray
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class UserInfoActivity : BaseActivity(), TakePhoto.TakeResultListener, InvokeListener {

    private var invokeParam: InvokeParam? = null
    private var takePhoto: TakePhoto? = null
    private var userInfo: UserInfo? = null

    private val options1Items = ArrayList<String>()
    private val options2Items = ArrayList<ArrayList<String>>()
    private val options3Items = ArrayList<ArrayList<ArrayList<String>>>()

    private var provinceId = 0
    private var cityId = 0
    private var areaId = 0

    override fun layoutId(): Int = R.layout.activity_user_info

    override fun initData() {
        userInfo = JMessageClient.getMyInfo().apply {
            this.getAvatarBitmap(object : GetAvatarBitmapCallback() {
                override fun gotResult(p0: Int, p1: String?, p2: Bitmap?) {
                    p2?.let {
                        ci_avatar.setImageBitmap(it)
                    }
                }
            })
            tv_username.text = this.userName
            this.getExtra("money")?.let {
                tv_money.text = it
            }
            tv_nickname.text = this.nickname
            tv_signature.text = this.signature
            tv_gender.text = getGender(this.gender)
            tv_birthday.text = AppUtil.unixTimestampToTime(this.birthday, "yyyy-MM-dd")
            tv_region.text = this.region
        }

        mTitleBar.setLeftLayoutClickListener(View.OnClickListener { finish() })
        rl_avatar.setOnClickListener { showOptionsDialog() }
        rl_nickname.setOnClickListener {
            InputDialog()
                    .title("昵称")
                    .defaultText(tv_nickname.text.toString())
                    .setOkClick(object : InputDialog.OnOkClickListener {
                        override fun onOkClick(string: String) {
                            tv_nickname.text = string
                            userInfo?.nickname = string
                            JMessageClient.updateMyInfo(UserInfo.Field.nickname, userInfo, null)
                        }
                    })
                    .show(supportFragmentManager, "")
        }
        rl_signature.setOnClickListener {
            InputDialog()
                    .title("签名")
                    .defaultText(tv_signature.text.toString())
                    .setOkClick(object : InputDialog.OnOkClickListener {
                        override fun onOkClick(string: String) {
                            tv_signature.text = string
                            userInfo?.signature = string
                            JMessageClient.updateMyInfo(UserInfo.Field.signature, userInfo, null)
                        }
                    })
                    .show(supportFragmentManager, "")
        }
        rl_gender.setOnClickListener {
            AlertDialog.Builder(mContext).setTitle("性别").setSingleChoiceItems(arrayOf("男", "女", "秘密"), getGenderType(tv_gender.text.toString())) { dialog, which ->
                when (which) {
                    0 -> {
                        tv_gender.text = getGender(UserInfo.Gender.male)
                        userInfo?.gender = UserInfo.Gender.male
                    }
                    1 -> {
                        tv_gender.text = getGender(UserInfo.Gender.female)
                        userInfo?.gender = UserInfo.Gender.female
                    }
                    2 -> {
                        tv_gender.text = getGender(UserInfo.Gender.unknown)
                        userInfo?.gender = UserInfo.Gender.unknown
                    }
                }
                JMessageClient.updateMyInfo(UserInfo.Field.gender, userInfo, null)
                dialog.cancel()
            }.show()
        }
        rl_birthday.setOnClickListener {
            val selectedDate = Calendar.getInstance()
            val sYear = tv_birthday.text.toString().split("-")[0].toInt()
            val sMonth = tv_birthday.text.toString().split("-")[1].toInt()
            val sDate = tv_birthday.text.toString().split("-")[2].toInt()
            val startDate = Calendar.getInstance()
            val endDate = Calendar.getInstance()
            startDate.set(1900, 0, 1)
            selectedDate.set(sYear, sMonth - 1, sDate)
            TimePickerBuilder(mContext, OnTimeSelectListener { date, _ ->
                tv_birthday.text = AppUtil.unixTimestampToTime(date.time, "yyyy-MM-dd")
                userInfo?.birthday = date.time
                JMessageClient.updateMyInfo(UserInfo.Field.birthday, userInfo, null)
            })
                    .setTitleBgColor(AppUtil.getThemeColor())
                    .setSubmitColor(Color.WHITE)
                    .setCancelColor(Color.WHITE)
                    .setTitleText("生日")
                    .setTitleColor(Color.WHITE)
                    .setTitleSize(23)
                    .setRangDate(startDate, endDate)
                    .setDate(selectedDate)
                    .build()
                    .show()
        }

        initRegion()
        rl_region.setOnClickListener {
            val pvOptions = OptionsPickerBuilder(mContext, OnOptionsSelectListener { options1, options2, options3, _ ->
                provinceId = options1
                cityId = options2
                areaId = options3
                tv_region.text = "${options1Items[options1]}-${options2Items[options1][options2]}-${options3Items[options1][options2][options3]}"
                userInfo?.region = tv_region.text.toString()
                JMessageClient.updateMyInfo(UserInfo.Field.region, userInfo, null)
            }).setTitleBgColor(AppUtil.getThemeColor())
                    .setSubmitColor(Color.WHITE)
                    .setCancelColor(Color.WHITE)
                    .setCyclic(true, true, true)
                    .setSelectOptions(provinceId, cityId, areaId)
                    .setTitleText("地区")
                    .setTitleColor(Color.WHITE)
                    .setTitleSize(23)
                    .build<Any>()
            pvOptions.setPicker(options1Items as List<Any>, options2Items as List<List<Any>>, options3Items as List<List<List<Any>>>)
            pvOptions.show()
        }
    }

    private fun initRegion() {
        val province = JSONArray(AppUtil.getJson("province.json"))
        for (i in 0 until province.length()) {
            val provinceEntity = Gson().fromJson(province.optJSONObject(i).toString(), ProvinceEntity::class.java)
            options1Items.add(provinceEntity.name)

            val cityList = ArrayList<String>()//该省的城市列表（第二级）
            val areaList = ArrayList<ArrayList<String>>()//该省的所有地区列表（第三极）
            for (j in 0 until provinceEntity.city.size) {
                cityList.add(provinceEntity.city[j].name)

                val cityAreaList = ArrayList<String>()//该城市的所有地区列表
                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (provinceEntity.city[j].county == null || provinceEntity.city[j].county.size == 0) {
                    cityAreaList.add("")
                } else {
                    for (k in 0 until provinceEntity.city[j].county.size) {
                        cityAreaList.add(provinceEntity.city[j].county[k].name)
                    }
                }
                areaList.add(cityAreaList)//添加该省所有地区数据
            }

            options2Items.add(cityList)
            options3Items.add(areaList)
        }
        //获得当前默认值
        if (!tv_region.text.isNullOrEmpty() && tv_region.text.toString().split("-").size == 3) {
            success@ for (i in 0 until options1Items.size) {
                if (options1Items[i] == tv_region.text.toString().split("-")[0]) {
                    provinceId = i
                    for (j in 0 until options2Items[i].size) {
                        if (options2Items[i][j] == tv_region.text.toString().split("-")[1]) {
                            cityId = j
                            for (k in 0 until options3Items[i][j].size) {
                                if (options3Items[i][j][k] == tv_region.text.toString().split("-")[2]) {
                                    areaId = k
                                    //跳出标记的循环
                                    break@success
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getGender(gender: UserInfo.Gender): String {
        return when (gender) {
            UserInfo.Gender.male -> "男"
            UserInfo.Gender.female -> "女"
            UserInfo.Gender.unknown -> "秘密"
        }
    }

    private fun getGenderType(gender: String): Int {
        when (gender) {
            "男" -> return 0
            "女" -> return 1
            "秘密" -> return 2
        }
        return 2
    }

    //选择图片来源
    private fun showOptionsDialog() {
        val items = arrayOf("相机", "相册")
        val file = File(mContext.externalCacheDir, System.currentTimeMillis().toString() + ".png")
        val uri = Uri.fromFile(file)
        val click = DialogInterface.OnClickListener { _, which ->
            //自定义裁剪选项
            val cropOptions = CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create()
            when (which) {
                //拍照
                0 -> {
                    //takePhoto.onPickFromCapture(uri);
                    takePhoto?.onPickFromCaptureWithCrop(uri, cropOptions)
                }
                //选择本地图片
                1 -> {
                    //takePhoto.onPickFromGallery();
                    takePhoto?.onPickFromGalleryWithCrop(uri, cropOptions)
                }
            }
        }
        AlertDialog.Builder(mContext).setItems(items, click).show().setCanceledOnTouchOutside(true)
    }

    override fun takeSuccess(result: TResult?) {
        Glide.with(mContext).load(result?.image?.originalPath).into(ci_avatar)
        updateAvatar(result?.image?.originalPath)
    }

    private fun updateAvatar(uri: String?) {
        val file = File(uri)//将要保存图片的路径
        JMessageClient.updateUserAvatar(file, object : BasicCallback() {
            override fun gotResult(p0: Int, p1: String?) {
                if (p0 == 0) {
                    EventBus.getDefault().post(MessageEvent(2, "更新资料"))
                    CustomToast.showSuccess("头像上传成功")
                } else {
                    CustomToast.showError("头像上传失败")
                }
                file.delete()
            }
        })
    }

    override fun takeCancel() {

    }

    override fun takeFail(result: TResult?, msg: String?) {

    }

    override fun invoke(invokeParam: InvokeParam?): PermissionManager.TPermissionType {
        val type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam!!.method)
        if (type == PermissionManager.TPermissionType.WAIT) {
            this.invokeParam = invokeParam
        }
        return type
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //以下代码为处理Android6.0、7.0动态权限所需
        val type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionManager.handlePermissionsResult(this, type, invokeParam, this)
    }

    private fun takePhoto(): TakePhoto {
        if (takePhoto == null) {
            takePhoto = TakePhotoInvocationHandler.of(this).bind(TakePhotoImpl(this, this)) as TakePhoto
        }
        return takePhoto as TakePhoto
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        takePhoto().onCreate(savedInstanceState)
        super.onCreate(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        takePhoto().onSaveInstanceState(outState)
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        takePhoto().onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        EventBus.getDefault().post(MessageEvent(2, "更新资料"))
        super.onDestroy()
    }
}