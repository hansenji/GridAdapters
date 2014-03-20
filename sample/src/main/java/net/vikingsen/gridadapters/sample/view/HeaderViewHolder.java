package net.vikingsen.gridadapters.sample.view;

import android.view.View;
import android.widget.TextView;

import net.vikingsen.gridadapters.sample.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Jordan Hansen
 */
public class HeaderViewHolder {
    @InjectView(R.id.textview)
    TextView textView;

    public HeaderViewHolder(View view) {
        ButterKnife.inject(this, view);
    }

    public TextView getTextView() {
        return textView;
    }
}
