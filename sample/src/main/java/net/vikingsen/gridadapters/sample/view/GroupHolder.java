package net.vikingsen.gridadapters.sample.view;

import android.view.View;
import android.widget.ImageView;

import net.vikingsen.gridadapters.sample.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;

/**
 * Created by Jordan Hansen
 */
public class GroupHolder {

    @InjectView(R.id.image_1)
    ImageView imageView1 = null;

    @InjectView(R.id.image_2)
    @Optional
    ImageView imageView2 = null;

    @InjectView(R.id.image_3)
    @Optional
    ImageView imageView3 = null;

    @InjectView(R.id.image_4)
    @Optional
    ImageView imageView4 = null;

    @InjectView(R.id.image_5)
    @Optional
    ImageView imageView5 = null;



    public GroupHolder(View v) {
        ButterKnife.inject(this, v);
    }

    public ImageView getImage(int position) {
        switch (position) {
            case 0:
                return imageView1;
            case 1:
                return imageView2;
            case 2:
                return imageView3;
            case 3:
                return imageView4;
            case 4:
                return imageView5;
            default:
                return null;
        }
    }
}
