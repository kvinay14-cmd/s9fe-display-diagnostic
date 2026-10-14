package com.example.s9fedisplaydiagnostic;

import android.app.Activity;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.Locale;

public class MainActivity extends Activity {
    private TextView output;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        ScrollView scroll = new ScrollView(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        int pad = (int) (20 * getResources().getDisplayMetrics().density);
        layout.setPadding(pad, pad, pad, pad);
        TextView title = new TextView(this);
        title.setText("S9 FE Display Diagnostic");
        title.setTextSize(24);
        layout.addView(title);
        output = new TextView(this);
        output.setTextSize(15);
        output.setPadding(0, pad, 0, pad);
        layout.addView(output);
        Button refresh = new Button(this);
        refresh.setText("Refresh display capabilities");
        refresh.setOnClickListener(v -> inspect());
        layout.addView(refresh);
        scroll.addView(layout);
        setContentView(scroll);
        inspect();
    }

    private void inspect() {
        StringBuilder s = new StringBuilder();
        s.append("DEVICE\nModel: ").append(Build.MODEL)
         .append("\nManufacturer: ").append(Build.MANUFACTURER)
         .append("\nAndroid: ").append(Build.VERSION.RELEASE)
         .append("\nSDK: ").append(Build.VERSION.SDK_INT)
         .append("\n\n");
        DisplayManager dm = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        Display[] displays = dm.getDisplays();
        s.append("DISPLAYS VISIBLE TO APP: ").append(displays.length).append("\n\n");
        for (Display d : displays) {
            Display.Mode m = d.getMode();
            s.append("Display ID: ").append(d.getDisplayId())
             .append("\nName: ").append(d.getName())
             .append("\nState: ").append(d.getState())
             .append("\nFlags: 0x").append(Integer.toHexString(d.getFlags()))
             .append("\nMode: ").append(m.getPhysicalWidth()).append("x")
             .append(m.getPhysicalHeight()).append("@")
             .append(String.format(Locale.US, "%.1f", m.getRefreshRate()))
             .append("Hz\n\n");
        }
        s.append("NOTE\nThis test lists displays visible to a normal app. It does not yet prove whether a true independent virtual display can be created.");
        output.setText(s.toString());
    }
}
