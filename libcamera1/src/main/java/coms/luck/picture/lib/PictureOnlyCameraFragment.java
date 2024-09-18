package coms.luck.picture.lib;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import coms.geek.libcamera1.R;
import coms.luck.picture.lib.basic.PictureCommonFragment;
import coms.luck.picture.lib.entity.LocalMedia;
import coms.luck.picture.lib.manager.SelectedManager;
import coms.luck.picture.lib.permissions.PermissionChecker;
import coms.luck.picture.lib.permissions.PermissionConfig;
import coms.luck.picture.lib.utils.SdkVersionUtils;
import coms.luck.picture.lib.utils.ToastUtils;

/**
 * @author：luck
 * @date：2021/11/22 2:26 下午
 * @describe：PictureOnlyCameraFragment
 */
public class PictureOnlyCameraFragment extends PictureCommonFragment {
    public static final String TAG = PictureOnlyCameraFragment.class.getSimpleName();

    public static PictureOnlyCameraFragment newInstance() {
        return new PictureOnlyCameraFragment();
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }

    @Override
    public int getResourceId() {
        return R.layout.gps_empty;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 这里只有非内存回收状态下才走，否则当内存不足Fragment被回收后会重复执行
        if (savedInstanceState == null) {
            openSelectedCamera();
        }
    }

    @Override
    public void dispatchCameraMediaResult(LocalMedia media) {
        int selectResultCode = confirmSelect(media, false);
        if (selectResultCode == SelectedManager.ADD_SUCCESS) {
            dispatchTransformResult();
        } else {
            onKeyBackFragmentFinish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            onKeyBackFragmentFinish();
        }
    }

    @Override
    public void handlePermissionSettingResult(String[] permissions) {
        onPermissionExplainEvent(false, null);
        boolean isHasPermissions;
        if (selectorConfig.onPermissionsEventListener != null) {
            isHasPermissions = selectorConfig.onPermissionsEventListener
                    .hasPermissions(this, permissions);
        } else {
            isHasPermissions = PermissionChecker.isCheckCamera(getContext());
            if (SdkVersionUtils.isQ()) {
            } else {
                isHasPermissions = PermissionChecker.isCheckWriteExternalStorage(getContext());
            }
        }
        if (isHasPermissions) {
            openSelectedCamera();
        } else {
            if (!PermissionChecker.isCheckCamera(getContext())) {
                ToastUtils.showToast(getContext(), getString(R.string.ps_camera));
            } else {
                if (!PermissionChecker.isCheckWriteExternalStorage(getContext())) {
                    ToastUtils.showToast(getContext(), getString(R.string.ps_jurisdiction));
                }
            }
            onKeyBackFragmentFinish();
        }
        PermissionConfig.CURRENT_REQUEST_PERMISSION = new String[]{};
    }
}
