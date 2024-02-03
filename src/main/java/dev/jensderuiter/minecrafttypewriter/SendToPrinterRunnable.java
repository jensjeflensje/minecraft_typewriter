package dev.jensderuiter.minecrafttypewriter;

import lombok.AllArgsConstructor;
import org.bukkit.scheduler.BukkitRunnable;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.swing.*;
import java.awt.print.PrinterException;

@AllArgsConstructor
public class SendToPrinterRunnable extends BukkitRunnable {

    String text;

    @Override
    public void run() {
        JEditorPane text = new JEditorPane("text/plain", this.text);
        PrintService service = PrintServiceLookup.lookupDefaultPrintService();
        try {
            text.print(null, null, false, service, null, false);
        } catch (PrinterException e) {
            throw new RuntimeException(e);
        }
    }
}
