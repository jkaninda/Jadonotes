package com.jkantech.jadonotes.ui.views

import android.annotation.SuppressLint
import android.content.*
import android.net.Uri
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.jkantech.jadonotes.BuildConfig
import com.jkantech.jadonotes.R
import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity(), View.OnClickListener {
    var appTheme=2
    lateinit var sharedPreferences: SharedPreferences
    private val themeKey = "currentTheme"
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(
                "ThemePref",
                Context.MODE_PRIVATE
        )

        applyStyle()
        setContentView(R.layout.activity_about)

        dev_mail_btn.setOnClickListener(this)
        dev_fb_btn.setOnClickListener(this)
        dev_gitub_btn.setOnClickListener(this)
        dev_whatsapp_btn.setOnClickListener(this)
        noterapp.setOnClickListener(this)
      //  source_code_btn.setOnClickListener(this)
        help_translate_btn.setOnClickListener(this)
        val versionCode:Int= BuildConfig.VERSION_CODE
        val versionName:String=BuildConfig.VERSION_NAME
        app_version.text="Version: "+versionName+" "+"($versionCode)"

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.dev_fb_btn -> {
                val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.facebook.com/jkantech")
                )
                startActivity(browserIntent)
            }
            R.id.dev_mail_btn -> {
                val intent = Intent(
                    Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", getString(R.string.dev_mail), null
                    )
                )
                val subject: String? = null
                intent.putExtra(Intent.EXTRA_SUBJECT, subject)
                val message: String? = null
                intent.putExtra(Intent.EXTRA_TEXT, message)
                startActivity(Intent.createChooser(intent, ""))
            }
            R.id.dev_gitub_btn -> {
                val githublien = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.github.com/jkantech")
                )
                startActivity(githublien)
            }
            R.id.dev_whatsapp_btn -> {
                val whatssap = Intent("android.intent.action.Main")
                whatssap.component = ComponentName("com.whatsapp", "com.whatsapp.Conversation")
                val stringBuilder = StringBuilder()
                stringBuilder.append(PhoneNumberUtils.stripSeparators(getString(R.string.dev_num)))
                stringBuilder.append("@s.whatsapp.net")
                whatssap.putExtra("jid", stringBuilder.toString())
                startActivity(whatssap)
            }
            /*
            R.id.source_code_btn->{
                val githublien = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.github.com/jkantech/jadonotes")
                )
                startActivity(githublien)
            }

             */
            R.id.help_translate_btn->{
                val github = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://crowdin.com/project/jadonotes")
                )
                startActivity(github)
            }




            R.id.noterapp -> try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$packageName")
                    )
                )
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
                    )
                )
            }
        }



    }

    override fun finish() {
        super.finish()
        overridePendingTransition(
            R.anim.activity_fade_in_animation,
            R.anim.activity_fade_out_animation
        )
    }
    private fun applyStyle() {

        when (sharedPreferences.getInt(themeKey, 0)) {

            0 -> theme.applyStyle(R.style.Theme_JadoNotes, true)
            1 -> theme.applyStyle(R.style.Theme1, true)
            2 -> theme.applyStyle(R.style.Theme2, true)
            3 -> theme.applyStyle(R.style.Theme3, true)

        }


    }
}