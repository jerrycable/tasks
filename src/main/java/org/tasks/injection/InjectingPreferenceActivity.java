package org.tasks.injection;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.tasks.R;
import org.tasks.preferences.ActivityPreferences;
import org.tasks.preferences.AppCompatPreferenceActivity;

import javax.inject.Inject;

import dagger.ObjectGraph;

public abstract class InjectingPreferenceActivity extends AppCompatPreferenceActivity implements Injector {

    private ObjectGraph objectGraph;

    protected Toolbar toolbar;

    @Inject
    ActivityPreferences activityPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        objectGraph = ((Injector) getApplication()).getObjectGraph().plus(new ActivityModule(this));
        inject(this);

        activityPreferences.applyThemeAndStatusBarColor();

        super.onCreate(savedInstanceState);

        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        View content = root.getChildAt(0);
        LinearLayout toolbarContainer = (LinearLayout) View.inflate(this, R.layout.activity_prefs, null);

        root.removeAllViews();
        toolbarContainer.addView(content);
        root.addView(toolbarContainer);

        toolbar = (Toolbar) toolbarContainer.findViewById(R.id.toolbar);
        toolbar.setTitle(getTitle());
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void inject(Object caller) {
        objectGraph.inject(caller);
    }

    @Override
    public ObjectGraph getObjectGraph() {
        return objectGraph;
    }
}
