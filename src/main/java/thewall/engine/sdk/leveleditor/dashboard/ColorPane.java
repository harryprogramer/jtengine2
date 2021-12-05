package thewall.engine.sdk.leveleditor.dashboard;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;

public class ColorPane extends JTextPane {

    public void appendNaive(Color c, String s) { // naive implementation
        // bad: instiantiates a new AttributeSet object on each call
        SimpleAttributeSet aset = new SimpleAttributeSet();
        StyleConstants.setForeground(aset, c);

        int len = getText().length();
        setCaretPosition(len); // place caret at the end (with no selection)
        setCharacterAttributes(aset, false);
        replaceSelection(s); // there is no selection, so inserts at caret
    }

    public void append(Color c, String s) { // better implementation--uses
        // StyleContext
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY,
                StyleConstants.Foreground, c);

        int len = getDocument().getLength(); // same value as
        // getText().length();
        setCaretPosition(len); // place caret at the end (with no selection)
        setCharacterAttributes(aset, false);
        replaceSelection(s); // there is no selection, so inserts at caret
    }

    public static boolean isPrime(int n) {
        if (n < 2)
            return false;
        double max = Math.sqrt(n);
        for (int j = 2; j <= max; j += 1)
            if (n % j == 0)
                return false; // j is a factor
        return true;
    }

    public static boolean isPerfectSquare(int n) {
        int j = 1;
        while (j * j < n && j * j > 0)
            j += 1;
        return (j * j == n);
    }

}
