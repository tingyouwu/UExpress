package com.wty.app.uexpress.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.wty.app.uexpress.BuildConfig;
import com.wty.app.uexpress.R;
import com.wty.app.uexpress.ui.BaseFragment;

/**
 * @author wty
 * 首页
 */
public class SettingFragment extends BaseFragment {

    public static final String TAG = "SettingFragment";

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void onInitView() {
        activity.getFragmentManager().beginTransaction().replace(R.id.ll_fragment_container, new AboutFragment()).commit();
    }

    @Override
    public void doWorkOnResume() {

    }

    @Override
    public void handleActionBar() {
        activity.getDefaultNavigation().setTitle(getString(R.string.about_us))
                .getLeftButton()
                .hide();
        activity.getDefaultNavigation().getRightButton().hide();
    }

    public static class AboutFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
        private Preference mVersion;
        private Preference mShare;
        private Preference mEmail;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference_about);

            mVersion = findPreference("version");
            mShare = findPreference("share");
            mEmail = findPreference("email");
            mVersion.setSummary("v " + BuildConfig.VERSION_NAME);
            setListener();
        }

        private void setListener() {
            mShare.setOnPreferenceClickListener(this);
            mEmail.setOnPreferenceClickListener(this);
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            if (preference == mShare) {
                shareApp();
                return true;
            }else if(preference == mEmail){
                sendEmail();
            }
            return false;
        }

        private void sendEmail(){
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL,
                    new String[] { "2421818708@qq.com" });
            i.putExtra(Intent.EXTRA_SUBJECT, "您的建议");
            i.putExtra(Intent.EXTRA_TEXT, "我们很希望能得到您的建议！！！");
            startActivity(Intent.createChooser(i,
                    "选择邮箱应用"));
        }

        private void shareApp(){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_content));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(Intent.createChooser(intent, getString(R.string.share)));
        }
    }

}
