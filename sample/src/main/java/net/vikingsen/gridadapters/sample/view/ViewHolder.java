package net.vikingsen.gridadapters.sample.view;

import android.view.View;
import android.widget.ImageView;

import net.vikingsen.gridadapters.sample.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Jordan Hansen
 */
public class ViewHolder {
    @InjectView(R.id.imageview)
    ImageView imageView;

    public ViewHolder(View view) {
        ButterKnife.inject(this, view);
    }

    public ImageView getImageView() {
        return imageView;
    }
}
