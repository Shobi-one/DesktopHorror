package logic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Point;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class HorrorGameController extends JFrame {
    // Core constants
    private static final String GAME_SIGNATURE = "FIND_OUR_FRIEND";
    private static final String[] CREEPY_MESSAGES = {
            "he is watching",
            "they're not just toys",
            "you shouldn't have opened this",
            "the shadow knows your name",
            "close it before it notices you",
            "your files aren't safe anymore",
            "did you hear that?",
            "it's in your system now",
            "they remember you now",
            "the rabbit lied to you"
    };

    // System information
    private static String systemUsername = System.getProperty("user.name");
    private static String userHome = System.getProperty("user.home");

    // GUI Components
    private JPanel eventsPanel;
    private JScrollPane scrollPane;
    private JButton resetButton;
    private JButton cleanupButton;
    private JLabel statusLabel;
    private JComboBox<String> messageSelector;
    private List<HorrorEvent> horrorEvents;

    public HorrorGameController() {
        // Setup main frame
        super("Find Our Friend - Developer Controller");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Initialize horror events
        initializeHorrorEvents();

        // Create GUI components
        createComponents();

        // Layout components
        layoutComponents();

        // Set frame visible
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeHorrorEvents() {
        horrorEvents = new ArrayList<>();

        // Add all possible horror events
        horrorEvents.add(new HorrorEvent("Create Creepy File", "Creates a text file with a creepy message on the desktop",
                () -> createCreepyFile("creepy_message.txt", getSelectedMessage())));

        horrorEvents.add(new HorrorEvent("Create Personalized File", "Creates a text file addressing the user by name",
                () -> createCreepyFile("for_you.txt", "Hello " + systemUsername + ", I've been waiting for you")));

        horrorEvents.add(new HorrorEvent("Open Notepad", "Opens notepad with a creepy message",
                () -> openCreepyNotepad(getSelectedMessage())));

        horrorEvents.add(new HorrorEvent("Open Camera", "Opens windows camera",
                () -> openCamera()));

        horrorEvents.add(new HorrorEvent("Find Location", "Opens Windows search and types 'What is my location'.",
                () -> locationFinder("what is my location")));

        horrorEvents.add(new HorrorEvent("Change Wallpaper", "Changes the desktop wallpaper to a creepy image",
                () -> changeWallpaperToCreepy()));

        horrorEvents.add(new HorrorEvent("Manipulate Windows", "Makes windows flash briefly",
                () -> manipulateWindows()));

        horrorEvents.add(new HorrorEvent("Play Sound", "Plays a creepy sound effect",
                () -> playCreepySound()));

        horrorEvents.add(new HorrorEvent("Toy Memory Fragment", "Creates a file with a memory of a childhood toy",
                () -> createToyMemoryFile()));

        horrorEvents.add(new HorrorEvent("Simulate System Alert", "Creates a fake system alert message",
                () -> createSystemAlert()));

        horrorEvents.add(new HorrorEvent("Flash Screen", "Briefly flashes the screen",
                () -> flashScreen()));

        horrorEvents.add(new HorrorEvent("Create Hidden Artifact", "Creates a hidden file in AppData",
                () -> createHiddenArtifact()));

        horrorEvents.add(new HorrorEvent("Screen Melt", "Creates a screenshot of the desktop, to then simulate melting.",
                () -> simulateDesktopMelting()));

        horrorEvents.add(new HorrorEvent("Blue Screen", "Simulate a BSOD.",
                () -> emulateBSOD()));
    }

    private void createComponents() {
        // Events panel with scroll pane
        eventsPanel = new JPanel();
        eventsPanel.setLayout(new BoxLayout(eventsPanel, BoxLayout.Y_AXIS));
        scrollPane = new JScrollPane(eventsPanel);

        // Message selector
        messageSelector = new JComboBox<>(CREEPY_MESSAGES);

        // Status label
        statusLabel = new JLabel("Ready to trigger events");

        // Reset and cleanup buttons
        resetButton = new JButton("Reset Game State");
        resetButton.addActionListener(e -> resetGameState());

        cleanupButton = new JButton("Cleanup All Files");
        cleanupButton.addActionListener(e -> cleanupAllFiles());

        // Create event buttons
        for (HorrorEvent event : horrorEvents) {
            JPanel eventPanel = createEventPanel(event);
            eventsPanel.add(eventPanel);
        }
    }

    private JPanel createEventPanel(HorrorEvent event) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        // Event details
        JPanel detailsPanel = new JPanel(new BorderLayout());
        JLabel nameLabel = new JLabel(event.getName());
        nameLabel.setFont(new Font("Dialog", Font.BOLD, 14));
        JLabel descLabel = new JLabel(event.getDescription());
        descLabel.setFont(new Font("Dialog", Font.PLAIN, 12));

        detailsPanel.add(nameLabel, BorderLayout.NORTH);
        detailsPanel.add(descLabel, BorderLayout.CENTER);

        // Trigger button
        JButton triggerButton = new JButton("Trigger");
        triggerButton.addActionListener(e -> {
            try {
                statusLabel.setText("Triggering: " + event.getName() + "...");
                event.execute();
                statusLabel.setText("Successfully triggered: " + event.getName());
            } catch (Exception ex) {
                statusLabel.setText("Error triggering " + event.getName() + ": " + ex.getMessage());
            }
        });

        panel.add(detailsPanel, BorderLayout.CENTER);
        panel.add(triggerButton, BorderLayout.EAST);

        return panel;
    }

    private void layoutComponents() {
        // Top panel with message selector
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Message to use:"));
        topPanel.add(messageSelector);
        add(topPanel, BorderLayout.NORTH);

        // Center with scrollable events
        add(scrollPane, BorderLayout.CENTER);

        // Bottom panel with status and buttons
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.add(resetButton);
        buttonsPanel.add(cleanupButton);

        bottomPanel.add(statusLabel, BorderLayout.WEST);
        bottomPanel.add(buttonsPanel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Helper method to get selected message
    private String getSelectedMessage() {
        return (String) messageSelector.getSelectedItem();
    }

    // Game state management
    private void resetGameState() {
        try {
            // Delete state file
            Path statePath = Paths.get(userHome, "AppData", "Local", "Temp", GAME_SIGNATURE + ".state");
            Files.deleteIfExists(statePath);

            // Delete signature file
            Path signaturePath = Paths.get(userHome, "AppData", "Local", "Temp", GAME_SIGNATURE + ".lock");
            Files.deleteIfExists(signaturePath);

            statusLabel.setText("Game state reset successfully");
        } catch (IOException e) {
            statusLabel.setText("Error resetting game state: " + e.getMessage());
        }
    }

    private void cleanupAllFiles() {
        try {
            // Cleanup desktop files
            Path desktop = Paths.get(userHome, "Desktop");
            try (java.nio.file.DirectoryStream<Path> stream = Files.newDirectoryStream(desktop)) {
                for (Path entry : stream) {
                    String filename = entry.getFileName().toString();
                    if (filename.endsWith(".txt") || filename.contains("creepy") ||
                            filename.contains("for_you") || filename.contains("toy")) {
                        Files.deleteIfExists(entry);
                    }
                }
            }

            // Cleanup temp files
            Path tempDir = Paths.get(userHome, "AppData", "Local", "Temp");
            try (java.nio.file.DirectoryStream<Path> stream = Files.newDirectoryStream(tempDir)) {
                for (Path entry : stream) {
                    String filename = entry.getFileName().toString();
                    if (filename.contains(GAME_SIGNATURE) || filename.startsWith("note")) {
                        Files.deleteIfExists(entry);
                    }
                }
            }

            // Cleanup any hidden artifacts
            Path hiddenDir = Paths.get(userHome, "AppData", "Roaming", "ToyBox");
            if (Files.exists(hiddenDir)) {
                Files.walk(hiddenDir)
                        .sorted(java.util.Comparator.reverseOrder())
                        .forEach(path -> {
                            try {
                                Files.deleteIfExists(path);
                            } catch (IOException e) {
                                // Ignore
                            }
                        });
            }

            statusLabel.setText("All game files cleaned up successfully");
        } catch (IOException e) {
            statusLabel.setText("Error cleaning up files: " + e.getMessage());
        }
    }

    // ==== HORROR EVENT IMPLEMENTATIONS ====

    private static void locationFinder(String query) {
        try {
            Robot robot = new Robot();

            // Open Windows search (Win + S is more reliable than just Win key)
            robot.keyPress(KeyEvent.VK_WINDOWS);
            robot.keyPress(KeyEvent.VK_S);
            robot.keyRelease(KeyEvent.VK_S);
            robot.keyRelease(KeyEvent.VK_WINDOWS);

            // Small delay to allow search bar to open
            robot.delay(500);

            // Type the query
            typeString(robot, query);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private static void typeString(Robot robot, String text) {
        for (char c : text.toCharArray()) {
            int keyCode = KeyEvent.getExtendedKeyCodeForChar(c);
            if (KeyEvent.CHAR_UNDEFINED == keyCode) continue;
            robot.keyPress(keyCode);
            robot.keyRelease(keyCode);
            robot.delay(50); // Small delay between key presses
        }
    }

    private void createCreepyFile(String filename, String content) {
        try {
            Path filePath = Paths.get(userHome, "Desktop", filename);
            Files.write(filePath, content.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to create file: " + e.getMessage());
        }
    }

    private void openCreepyNotepad(String message) {
        try {
            // Create temp file
            File tempFile = File.createTempFile("note", ".txt");
            Files.write(tempFile.toPath(), message.getBytes());

            // Open in notepad
            Runtime.getRuntime().exec("notepad.exe " + tempFile.getAbsolutePath());

            // Schedule deletion after some time
            new Timer(30000, e -> tempFile.delete()).start();
        } catch (IOException e) {
            throw new RuntimeException("Failed to open notepad: " + e.getMessage());
        }
    }

    private void openCamera() {
        try {
            if (System.getProperty("os.name").startsWith("Windows")) {
                // Using PowerShell with explicit camera URI
                Runtime.getRuntime().exec(new String[]{
                        "powershell.exe",
                        "-Command",
                        "start microsoft.windows.camera:"
                });
            } else {
                throw new RuntimeException("Camera feature is only supported on Windows");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to open camera: " + e.getMessage());
        }
    }

    private void changeWallpaperToCreepy() {
        // In a real implementation, this would use a pre-packaged wallpaper from resources
        try {
            JOptionPane.showMessageDialog(this,
                    "This would change the wallpaper in a real implementation.\n" +
                            "For now, this is just a simulation of the functionality.",
                    "Wallpaper Change Simulated",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            throw new RuntimeException("Failed to change wallpaper: " + e.getMessage());
        }
    }

    private void manipulateWindows() {
        try {
            // Flash this window as a demonstration
            for (int i = 0; i < 3; i++) {
                this.setVisible(false);
                Thread.sleep(100);
                this.setVisible(true);
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Failed to manipulate windows: " + e.getMessage());
        }
    }

    private void playCreepySound() {
        // In a real implementation, this would play a sound from resources
        try {
            Toolkit.getDefaultToolkit().beep(); // Simple beep as placeholder
            JOptionPane.showMessageDialog(this,
                    "This would play a creepy sound in a real implementation.\n" +
                            "For now, you just heard a system beep as placeholder.",
                    "Sound Effect Simulated",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            throw new RuntimeException("Failed to play sound: " + e.getMessage());
        }
    }

    private void createToyMemoryFile() {
        String[] toyMemories = {
                "I still remember the day you lost Mr. Snuggles. But he didn't forget you.",
                "Did you ever wonder where your childhood toys went when you grew up?",
                "The music box still plays our lullaby when no one is listening.",
                "Your toys remember the darkness under your bed better than you do.",
                "We used to have tea parties, don't you remember? You promised we'd be friends forever."
        };

        int index = (int)(Math.random() * toyMemories.length);
        createCreepyFile("toy_memory.txt", toyMemories[index]);
    }

    private void createSystemAlert() {
        JOptionPane.showMessageDialog(this,
                "Warning: Unknown process accessing system files",
                "System Security Alert",
                JOptionPane.WARNING_MESSAGE);
    }

    private void flashScreen() {
        try {
            // Create a white frame that covers the screen
            JFrame flashFrame = new JFrame();
            flashFrame.setUndecorated(true);
            flashFrame.setBackground(new Color(255, 255, 255, 200));
            flashFrame.setAlwaysOnTop(true);

            // Make it cover the entire screen
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            flashFrame.setSize(screenSize);

            // Show and hide quickly
            flashFrame.setVisible(true);

            // Hide after 100ms
            new Timer(100, e -> {
                flashFrame.setVisible(false);
                flashFrame.dispose();
            }).start();
        } catch (Exception e) {
            throw new RuntimeException("Failed to flash screen: " + e.getMessage());
        }
    }



    private static void simulateDesktopMelting() {
        try {
            // Create a transparent window that covers the entire screen
            JFrame meltFrame = new JFrame();
            meltFrame.setUndecorated(true);
            meltFrame.setBackground(new Color(0, 0, 0, 0));
            meltFrame.setAlwaysOnTop(true);

            // Make it cover the entire screen
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            meltFrame.setSize(screenSize);

            // Create a custom panel for the melting animation
            JPanel meltPanel = new JPanel() {
                private int animationStep = 0;
                private final int MAX_STEPS = 50;
                private Robot robot;
                private BufferedImage screenshot;

                {
                    try {
                        // Take a screenshot of the current desktop
                        robot = new Robot();
                        screenshot = robot.createScreenCapture(new Rectangle(screenSize));

                        // Start animation timer
                        Timer timer = new Timer(50, e -> {
                            animationStep++;
                            if (animationStep > MAX_STEPS) {
                                ((Timer)e.getSource()).stop();
                                meltFrame.dispose();
                            }
                            repaint();
                        });
                        timer.start();
                    } catch (AWTException e) {
                        // Fail silently in keeping with the rest of the code
                    }
                }

                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (screenshot == null) return;

                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                    // Apply melting effect
                    for (int x = 0; x < screenSize.width; x += 10) {
                        // Calculate how much this column has melted
                        // We'll make melting more random for added creepiness
                        float meltFactor = (float)x / screenSize.width;
                        // Add some randomness to the melt factor
                        meltFactor *= (0.5f + (float)Math.random() * 0.5f);
                        int meltOffset = (int)(animationStep * 15 * meltFactor);

                        // Draw slices of the screenshot with increasing downward offsets
                        for (int y = 0; y < screenSize.height; y += 5) {
                            int sourceY = y;
                            int targetY = Math.min(screenSize.height, y + meltOffset);

                            // As we melt, stretch the pixels
                            if (y + meltOffset < screenSize.height) {
                                // Draw slice of the screenshot at the melted position
                                g2d.drawImage(screenshot,
                                        x, targetY, x + 10, targetY + 5,
                                        x, sourceY, x + 10, sourceY + 5,
                                        null);
                            }
                        }
                    }

                    // Add a reddish tint that increases with animation
                    float alpha = (float)animationStep / MAX_STEPS * 0.3f;
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
                    g2d.setColor(new Color(255, 0, 0));
                    g2d.fillRect(0, 0, getWidth(), getHeight());
                }
            };

            meltFrame.setContentPane(meltPanel);
            meltFrame.setVisible(true);

            // Automatically dispose after 5 seconds as a safety measure
            // Use java.util.Timer since we're not in Swing's event dispatch thread
            new java.util.Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    meltFrame.dispose();
                }
            }, 5000);

        } catch (Exception e) {
            // Fail silently in keeping with the rest of the code
        }
    }


    private void createHiddenArtifact() {
        try {
            // Create hidden directory if it doesn't exist
            Path hiddenDir = Paths.get(userHome, "AppData", "Roaming", "ToyBox");
            if (!Files.exists(hiddenDir)) {
                Files.createDirectories(hiddenDir);
            }

            // Create a hidden file with creepy content
            String[] hiddenContents = {
                    "Subject 7 continues to show unusual attachment to the plush items.",
                    "The experiment was never meant to go this far.",
                    "We've lost contact with the night staff. Security footage shows nothing unusual.",
                    "The toys have been exhibiting unexpected behavior when not under observation.",
                    "Experiment terminated. All materials to be incinerated immediately."
            };

            int index = (int)(Math.random() * hiddenContents.length);
            String filename = "log_" + System.currentTimeMillis() + ".txt";
            Path filePath = hiddenDir.resolve(filename);

            Files.write(filePath, hiddenContents[index].getBytes());
            Files.setAttribute(filePath, "dos:hidden", true);

            JOptionPane.showMessageDialog(this,
                    "Hidden artifact created at:\n" + filePath.toString(),
                    "Hidden Artifact Created",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create hidden artifact: " + e.getMessage());
        }
    }

    private static void emulateBSOD() {
        try {
            // Create a full screen window to simulate BSOD
            JFrame bsodFrame = new JFrame();
            bsodFrame.setUndecorated(true);
            bsodFrame.setAlwaysOnTop(true);

            // Get screen dimensions
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            bsodFrame.setSize(screenSize);

            // Create a panel for the BSOD content
            JPanel bsodPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);

                    // Set blue background
                    g.setColor(new Color(0, 120, 215)); // Windows 10/11 blue
                    g.fillRect(0, 0, getWidth(), getHeight());

                    // Set white text
                    g.setColor(Color.WHITE);

                    // Use a monospaced font to mimic system font
                    Font mainFont = new Font("Segoe UI", Font.PLAIN, 24);
                    Font smallFont = new Font("Segoe UI", Font.PLAIN, 16);
                    Font titleFont = new Font("Segoe UI", Font.BOLD, 70);
                    Font uiFont = new Font("Segoe UI", Font.BOLD, 128);


                    // Draw sad face - Windows 10/11 style
                    g.setFont(uiFont);
                    g.drawString(":(", screenSize.width / 12, screenSize.height / 4);

                    // Draw error text
                    g.setFont(mainFont);
                    g.drawString("Your PC ran into a problem and needs to restart.",
                            screenSize.width / 12, screenSize.height / 4 + 100);
                    g.drawString("We're just collecting some error info, and then we'll restart for you.",
                            screenSize.width / 12, screenSize.height / 4 + 140);

                    // Draw progress
                    g.drawString("100% complete",
                            screenSize.width / 12, screenSize.height / 4 + 200);

                    // Draw fake error code
                    g.setFont(smallFont);
                    String[] errorInfo = {
                            "For more information about this issue and possible fixes, visit",
                            "https://www.windows.com/stopcode",
                            "",
                            "If you call a support person, give them this info:",
                            "Stop code: CORRUPTED_TOY_DATA"
                    };

                    int y = screenSize.height / 4 + 280;
                    for (String line : errorInfo) {
                        g.drawString(line, screenSize.width / 12, y);
                        y += 24;
                    }

                    // Draw QR code placeholder
                    g.drawRect(screenSize.width / 12, screenSize.height / 4 + 420, 100, 100);
                    g.drawLine(screenSize.width / 12, screenSize.height / 4 + 420,
                            screenSize.width / 12 + 100, screenSize.height / 4 + 520);
                    g.drawLine(screenSize.width / 12 + 100, screenSize.height / 4 + 420,
                            screenSize.width / 12, screenSize.height / 4 + 520);
                }
            };

            bsodPanel.setBackground(new Color(0, 120, 215)); // Windows 10/11 blue
            bsodFrame.setContentPane(bsodPanel);

            // Make cursor invisible
            Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                    new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB),
                    new Point(0, 0),
                    "blank");
            bsodPanel.setCursor(blankCursor);

            // Show the BSOD
            bsodFrame.setVisible(true);

            // Disable Alt+F4 by consuming the key event
            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
                if (e.getID() == KeyEvent.KEY_PRESSED) {
                    if (e.isAltDown() && e.getKeyCode() == KeyEvent.VK_F4) {
                        return true; // Consume Alt+F4
                    }
                }
                return false;
            });

            // Make it stay for a few seconds (adjust time as needed)
            int displayTimeMs = 8000; // Show for 8 seconds
            new java.util.Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    bsodFrame.dispose();
                }
            }, displayTimeMs);

        } catch (Exception e) {
            // Fail silently
        }
    }



    // Class to represent a horror event
    private static class HorrorEvent {
        private String name;
        private String description;
        private Runnable action;

        public HorrorEvent(String name, String description, Runnable action) {
            this.name = name;
            this.description = description;
            this.action = action;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public void execute() {
            action.run();
        }
    }

    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create controller
        SwingUtilities.invokeLater(() -> new HorrorGameController());
    }
}