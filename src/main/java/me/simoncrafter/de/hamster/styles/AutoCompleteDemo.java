package me.simoncrafter.de.hamster.styles;

import org.fife.rsta.ac.LanguageSupportFactory;
import org.fife.rsta.ac.java.JavaLanguageSupport;
import org.fife.ui.autocomplete.*;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class AutoCompleteDemo extends JFrame {

    public AutoCompleteDemo() {

        JPanel contentPane = new JPanel(new BorderLayout());
        RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);
        textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        textArea.setCodeFoldingEnabled(true);
        contentPane.add(new RTextScrollPane(textArea));

        textArea.setCodeFoldingEnabled(true);
        JavaLanguageSupport support = (JavaLanguageSupport) LanguageSupportFactory.get()
                .getSupportFor(SyntaxConstants.SYNTAX_STYLE_JAVA);

        // Optional: enable auto popup
        support.setAutoActivationEnabled(true);
        support.setAutoActivationDelay(200);

        // Add the JDK source/classes (so String, List, etc. work)
        File javaHome = new File(System.getProperty("java.home"));
        File libDir = new File(javaHome, "lib");
        File[] jars = libDir.listFiles((dir, name) -> name.endsWith(".jar"));

        if (jars != null) {
            for (File jar : jars) {
                try {
                    support.getJarManager().addClassFileSource(jar);
                }catch (Exception e) {
                    System.out.println("Error in text editor:");
                    e.printStackTrace();
                }
            }
        }

        // Install on the text area
        support.install(textArea);

        // A CompletionProvider is what knows of all possible completions, and
        // analyzes the contents of the text area at the caret position to
        // determine what completion choices should be presented. Most instances
        // of CompletionProvider (such as DefaultCompletionProvider) are designed
        // so that they can be shared among multiple text components.
        CompletionProvider provider = createCompletionProvider();

        // An AutoCompletion acts as a "middle-man" between a text component
        // and a CompletionProvider. It manages any options associated with
        // the auto-completion (the popup trigger key, whether to display a
        // documentation window along with completion choices, etc.). Unlike
        // CompletionProviders, instances of AutoCompletion cannot be shared
        // among multiple text components.

        AutoCompletion ac = new AutoCompletion(provider);
        ac.setAutoCompleteSingleChoices(true);
        ac.install(textArea);


        textArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void showCompletions() {
                SwingUtilities.invokeLater(() -> {
                    try {
                        ac.doCompletion(); // force show all completions
                    } catch (Exception ignored) {}
                });
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                ac.doCompletion();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
            }
        });

        setContentPane(contentPane);
        setTitle("AutoComplete Demo");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

    }

    /**
     * Create a simple provider that adds some Java-related completions.
     */
    private CompletionProvider createCompletionProvider() {

        // A DefaultCompletionProvider is the simplest concrete implementation
        // of CompletionProvider. This provider has no understanding of
        // language semantics. It simply checks the text entered up to the
        // caret position for a match against known completions. This is all
        // that is needed in the majority of cases.
        DefaultCompletionProvider provider = new DefaultCompletionProvider();



        // Add completions for all Java keywords. A BasicCompletion is just
        // a straightforward word completion.
        /*provider.addCompletion(new BasicCompletion(provider, "abstract"));
        provider.addCompletion(new BasicCompletion(provider, "assert"));
        provider.addCompletion(new BasicCompletion(provider, "break"));
        provider.addCompletion(new BasicCompletion(provider, "case"));
        // ... etc ...
        provider.addCompletion(new BasicCompletion(provider, "transient"));
        provider.addCompletion(new BasicCompletion(provider, "try"));
        provider.addCompletion(new BasicCompletion(provider, "void"));
        provider.addCompletion(new BasicCompletion(provider, "volatile"));
        provider.addCompletion(new BasicCompletion(provider, "while"));

        provider.setAutoActivationRules(true, "abcdefghijklmnopqrstuvwxyz.");
        // Add a couple of "shorthand" completions. These completions don't
        // require the input text to be the same thing as the replacement text.
        provider.addCompletion(new ShorthandCompletion(provider, "sysout",
                "System.out.println(", "System.out.println("));
        provider.addCompletion(new ShorthandCompletion(provider, "syserr",
                "System.err.println(", "System.err.println("));*/
        LanguageAwareCompletionProvider p = new LanguageAwareCompletionProvider(provider);

        p.setStringCompletionProvider(provider);
        p.setCommentCompletionProvider(provider);

        return p;

    }

    public static void main(String[] args) {
        // Instantiate GUI on the EDT.
        SwingUtilities.invokeLater(() -> {
            try {
                String laf = UIManager.getSystemLookAndFeelClassName();
                UIManager.setLookAndFeel(laf);
            } catch (Exception e) { /* Never happens */ }
            new AutoCompleteDemo().setVisible(true);
        });
    }

}
