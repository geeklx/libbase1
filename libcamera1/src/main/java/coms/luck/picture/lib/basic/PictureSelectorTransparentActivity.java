package coms.luck.picture.lib.basic;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import coms.luck.picture.lib.PictureOnlyCameraFragment;
import coms.luck.picture.lib.PictureSelectorPreviewFragment;
import coms.luck.picture.lib.PictureSelectorSystemFragment;
import coms.geek.libcamera1.R;
import coms.luck.picture.lib.config.PictureConfig;
import coms.luck.picture.lib.config.SelectorConfig;
import coms.luck.picture.lib.config.SelectorProviders;
import coms.luck.picture.lib.entity.LocalMedia;
import coms.luck.picture.lib.style.PictureWindowAnimationStyle;
import coms.luck.picture.lib.style.SelectMainStyle;
import coms.luck.picture.lib.utils.StyleUtils;
import coms.luck.picture.lib.immersive.ImmersiveManager;

import java.util.ArrayList;

/**
 * @author：luck
 * @date：2022/2/10 6:07 下午
 * @describe：PictureSelectorTransparentActivity
 */
public class PictureSelectorTransparentActivity extends AppCompatActivity {
    private SelectorConfig selectorConfig;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSelectorConfig();
        immersive();
        setContentView(R.layout.gps_empty);
        if (isExternalPreview()) {
            // TODO ignore
        } else {
            setActivitySize();
        }
        setupFragment();
    }

    private void initSelectorConfig() {
        selectorConfig = SelectorProviders.getInstance().getSelectorConfig();
    }

    private boolean isExternalPreview() {
        int modeTypeSource = getIntent().getIntExtra(PictureConfig.EXTRA_MODE_TYPE_SOURCE, 0);
        return modeTypeSource == PictureConfig.MODE_TYPE_EXTERNAL_PREVIEW_SOURCE;
    }

    private void immersive() {
        if (selectorConfig.selectorStyle == null) {
            SelectorProviders.getInstance().getSelectorConfig();
        }
        SelectMainStyle mainStyle = selectorConfig.selectorStyle.getSelectMainStyle();
        int statusBarColor = mainStyle.getStatusBarColor();
        int navigationBarColor = mainStyle.getNavigationBarColor();
        boolean isDarkStatusBarBlack = mainStyle.isDarkStatusBarBlack();
        if (!StyleUtils.checkStyleValidity(statusBarColor)) {
            statusBarColor = ContextCompat.getColor(this, R.color.ps_color_grey);
        }
        if (!StyleUtils.checkStyleValidity(navigationBarColor)) {
            navigationBarColor = ContextCompat.getColor(this, R.color.ps_color_grey);
        }
        ImmersiveManager.immersiveAboveAPI23(this, statusBarColor, navigationBarColor, isDarkStatusBarBlack);
    }

    private void setupFragment() {
        String fragmentTag;
        Fragment targetFragment = null;
        int modeTypeSource = getIntent().getIntExtra(PictureConfig.EXTRA_MODE_TYPE_SOURCE, 0);
        if (modeTypeSource == PictureConfig.MODE_TYPE_SYSTEM_SOURCE) {
            fragmentTag = PictureSelectorSystemFragment.TAG;
            targetFragment = PictureSelectorSystemFragment.newInstance();
        } else if (modeTypeSource == PictureConfig.MODE_TYPE_EXTERNAL_PREVIEW_SOURCE) {
            if (selectorConfig.onInjectActivityPreviewListener != null) {
                targetFragment = selectorConfig.onInjectActivityPreviewListener.onInjectPreviewFragment();
            }
            if (targetFragment != null) {
                fragmentTag = ((PictureSelectorPreviewFragment) targetFragment).getFragmentTag();
            } else {
                fragmentTag = PictureSelectorPreviewFragment.TAG;
                targetFragment = PictureSelectorPreviewFragment.newInstance();
            }
            int position = getIntent().getIntExtra(PictureConfig.EXTRA_PREVIEW_CURRENT_POSITION, 0);
            ArrayList<LocalMedia> previewData = new ArrayList<>(selectorConfig.selectedPreviewResult);
            boolean isDisplayDelete = getIntent()
                    .getBooleanExtra(PictureConfig.EXTRA_EXTERNAL_PREVIEW_DISPLAY_DELETE, false);
            ((PictureSelectorPreviewFragment) targetFragment).setExternalPreviewData(position, previewData.size(), previewData, isDisplayDelete);
        } else {
            fragmentTag = PictureOnlyCameraFragment.TAG;
            targetFragment = PictureOnlyCameraFragment.newInstance();
        }
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        Fragment fragment = supportFragmentManager.findFragmentByTag(fragmentTag);
        if (fragment != null) {
            supportFragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss();
        }
        FragmentInjectManager.injectSystemRoomFragment(supportFragmentManager, fragmentTag, targetFragment);
    }

    @SuppressLint("RtlHardcoded")
    private void setActivitySize() {
        Window window = getWindow();
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.height = 1;
        params.width = 1;
        window.setAttributes(params);
    }

    @Override
    public void finish() {
        super.finish();
        int modeTypeSource = getIntent().getIntExtra(PictureConfig.EXTRA_MODE_TYPE_SOURCE, 0);
        if (modeTypeSource == PictureConfig.MODE_TYPE_EXTERNAL_PREVIEW_SOURCE && !selectorConfig.isPreviewZoomEffect) {
            PictureWindowAnimationStyle windowAnimationStyle = selectorConfig.selectorStyle.getWindowAnimationStyle();
            overridePendingTransition(0, windowAnimationStyle.activityExitAnimation);
        } else {
            overridePendingTransition(0, R.anim.ps_anim_fade_out);
        }
    }
}
