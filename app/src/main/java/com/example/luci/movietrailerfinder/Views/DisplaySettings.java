package com.example.luci.movietrailerfinder.Views;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.luci.movietrailerfinder.R;
import com.example.luci.movietrailerfinder.Data.ViewType;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Dialog that provides interface and functionality for view type selection
 */
public class DisplaySettings extends DialogFragment {

    /**
     * Custom callback for view type selection
     */
    public interface ViewTypeSelectedCallback{
        void onViewTypeSelected(ViewType viewType);
    }

    /** The reference of view type select */
    private ViewTypeSelectedCallback viewTypeSelectedCallback;

    /** The current view type  */
    private ViewType currentViewType;

    /** The image representing list */
    @BindView(R.id.display_settings_dialog_list)
    ImageView listTypeImage;

    /** The image representing carousel */
    @BindView(R.id.display_settings_dialog_carousel)
    ImageView carouselTypeImage;

    /**
     * Closes the dialog after view type was selected
     */
    private void changeViewType() {
        viewTypeSelectedCallback.onViewTypeSelected(currentViewType);
        DisplaySettings.this.dismiss();
    }

    @OnClick({R.id.display_settings_dialog_carousel})
    void carousel_view_type(@SuppressWarnings("unused") View view){
        currentViewType = ViewType.CAROUSEL;
        changeViewType();
    }

    @OnClick({R.id.display_settings_dialog_list})
    void list_view_type(@SuppressWarnings("unused") View view){
        currentViewType = ViewType.LIST;
        changeViewType();
    }

    public void setSelectViewTypeCallback(ViewTypeSelectedCallback viewTypeCallback){
        this.viewTypeSelectedCallback = viewTypeCallback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DisplaySettings.this.setCancelable(false);
        View rootView = inflater.inflate(R.layout.display_option_fragment, container, false);
        ButterKnife.bind(this,rootView);
        return rootView;
    }
}
