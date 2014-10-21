package net.ShortKey;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

public class Main extends Activity {
    ToggleButton toggle;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        toggle = (ToggleButton) findViewById(R.id.toggleButton);
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toggle.isChecked()) {
                    ImageView imageView = (ImageView) findViewById(R.id.imageView2323);
                    imageView.setImageResource(R.drawable.home_shortkey);

                    TextSwitcher valueView = (TextSwitcher) findViewById(R.id.serviceStatus);

                    try {
                        valueView.setFactory(new ViewSwitcher.ViewFactory() {

                            public View makeView() {
                                TextView myText = new TextView(getBaseContext());
                                myText.setGravity(Gravity.CENTER);
                                myText.setTextColor(getBaseContext().getResources().getColor(R.color.service_status_fore_color));
                                myText.setTextAppearance(getBaseContext(), android.R.style.TextAppearance_DeviceDefault_Medium);
                                return myText;
                            }
                        });
                        Animation in = AnimationUtils.loadAnimation(getBaseContext(), android.R.anim.fade_in);
                        Animation out = AnimationUtils.loadAnimation(getBaseContext(), android.R.anim.fade_out);

                        valueView.setInAnimation(in);
                        valueView.setOutAnimation(out);
                    } catch (Exception ex) {
                    }

                    valueView.setText("سریس فعال است");

                } else {
                    ImageView imageView = (ImageView) findViewById(R.id.imageView2323);
                    imageView.setImageResource(R.drawable.home_shortkey_grayscale);


                    TextSwitcher valueView = (TextSwitcher) findViewById(R.id.serviceStatus);

                    try {
                        valueView.setFactory(new ViewSwitcher.ViewFactory() {

                            public View makeView() {
                                TextView myText = new TextView(getBaseContext());
                                myText.setGravity(Gravity.CENTER);
                                myText.setTextColor(getBaseContext().getResources().getColor(R.color.service_status_fore_color));
                                myText.setTextAppearance(getBaseContext(), android.R.style.TextAppearance_DeviceDefault_Medium);
                                return myText;
                            }
                        });
                        Animation in = AnimationUtils.loadAnimation(getBaseContext(), android.R.anim.fade_in);
                        Animation out = AnimationUtils.loadAnimation(getBaseContext(), android.R.anim.fade_out);

                        valueView.setInAnimation(in);
                        valueView.setOutAnimation(out);
                    } catch (Exception ex) {
                    }

                    valueView.setText("سرویس غیرفعال است");
                }
            }
        });

//        new MultiLanguage().setLanguage();
//
//        getFragmentManager().beginTransaction().replace(android.R.id.content,
//                new SettingsFragment()).commit();
//
//        new ShortKeyServiceController().ToggleServiceStatus();
    }
}
