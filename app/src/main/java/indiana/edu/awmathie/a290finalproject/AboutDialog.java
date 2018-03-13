package indiana.edu.awmathie.a290finalproject;

import android.app.AlertDialog;
import android.content.Context;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

/**
 * Created by awmathie on 2/28/2018.
 * Custom dialog object for information about the app, activates any links in the text
 */

public class AboutDialog {

    public static AlertDialog create(Context context) {
        final TextView message = new TextView(context);
        final SpannableString s =
                new SpannableString(context.getText(R.string.AboutText));
        Linkify.addLinks(s, Linkify.WEB_URLS);
        message.setText(s);
        message.setMovementMethod(LinkMovementMethod.getInstance());

        return new AlertDialog.Builder(context)
                .setCancelable(true)
                .setView(message)
                .create();
    }
}
