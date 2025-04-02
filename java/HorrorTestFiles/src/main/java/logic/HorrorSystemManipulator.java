package logic;

import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HorrorSystemManipulator {
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

    private static final String[] CORRUPTED_FILENAMES = {
            "hello.txt",
    };

    private static boolean isActive = true;
    private static Random random = new Random();
    private static String systemUsername = System.getProperty("user.name");
    private static String userHome = System.getProperty("user.home");

    public static void main(String[] args) {
        // Check if this is the first run
        if (!checkForExistingSignature()) {
            createSignatureFile();
            startHorrorEffects();
        } else {
            // If signature exists, escalate effects
            escalateHorrorEffects();
        }
    }

    private static boolean checkForExistingSignature() {
        Path signaturePath = Paths.get(userHome, "AppData", "Local", "Temp", GAME_SIGNATURE + ".lock");
        return Files.exists(signaturePath);
    }

    private static void createSignatureFile() {
        try {
            Path signaturePath = Paths.get(userHome, "AppData", "Local", "Temp", GAME_SIGNATURE + ".lock");
            Files.write(signaturePath, "This system has been marked".getBytes());
            Files.setAttribute(signaturePath, "dos:hidden", true);
        } catch (IOException e) {

        }
    }

    private static void startHorrorEffects() {
        // Initial subtle effects
        Timer timer = new Timer();

        // Phase 1: Subtle disturbances
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isActive) return;

                // Random chance to create a creepy file
                if (random.nextInt(100) < 20) {
                    createCreepyFile();
                }

                // Occasionally change desktop (subtle)
                if (random.nextInt(100) < 10) {
                    subtlyChangeWallpaper();
                }
            }
        }, 0, 30000); // Every 30 seconds

        // Phase 2: More noticeable effects (after 5 minutes)
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                escalateEffects();
            }
        }, 300000);

        // Phase 3: Aggressive effects (after 10 minutes)
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                aggressiveEffects();
            }
        }, 600000);
    }

    private static void escalateHorrorEffects() {
        // More aggressive effects if this isn't the first run
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isActive) return;

                // Higher chance for creepy files
                if (random.nextInt(100) < 40) {
                    createCreepyFile();
                }

                // More noticeable wallpaper changes
                if (random.nextInt(100) < 20) {
                    changeWallpaperToCreepy();
                }

                // Sometimes open notepad with creepy message
                if (random.nextInt(100) < 15) {
                    openCreepyNotepad();
                }
            }
        }, 0, 15000); // Every 15 seconds

        // Fast-track to aggressive effects
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                aggressiveEffects();
            }
        }, 120000); // After 2 minutes
    }

    private static void createCreepyFile() {
        try {
            String filename = CORRUPTED_FILENAMES[random.nextInt(CORRUPTED_FILENAMES.length)];
            Path filePath;

            // 50% chance for desktop, 50% for documents
            if (random.nextBoolean()) {
                filePath = Paths.get(userHome, "Desktop", filename);
            } else {
                filePath = Paths.get(userHome, "Documents", filename);
            }

            String message = CREEPY_MESSAGES[random.nextInt(CREEPY_MESSAGES.length)];
            if (random.nextInt(100) < 30) {
                message = message.toUpperCase();
            }

            Files.write(filePath, message.getBytes());

            // Sometimes make it hidden
            if (random.nextBoolean()) {
                Files.setAttribute(filePath, "dos:hidden", true);
            }
        } catch (IOException e) {
            // Fail silently
        }
    }

    private static void subtlyChangeWallpaper() {
        try {
            // Get current wallpaper
            String currentWallpaper = System.getProperty("user.home") + "\\AppData\\Roaming\\Microsoft\\Windows\\Themes\\TranscodedWallpaper";

            // Make a barely noticeable change (rotate 1 degree)
            if (Files.exists(Paths.get(currentWallpaper))) {
                // In a real implementation, you'd use image manipulation here
                // This is just a placeholder for the concept
            }
        } catch (Exception e) {
            // Fail silently
        }
    }

    private static void changeWallpaperToCreepy() {
        try {
            // This would require actual image files bundled with the game
            // Placeholder for the concept:
            File imageFile = getRandomCreepyImage();
            if (imageFile != null && imageFile.exists()) {
                String script = "reg add \"HKEY_CURRENT_USER\\Control Panel\\Desktop\" /v Wallpaper /t REG_SZ /d \"" +
                        imageFile.getAbsolutePath() + "\" /f";
                Runtime.getRuntime().exec(script);
                Runtime.getRuntime().exec("RUNDLL32.EXE user32.dll,UpdatePerUserSystemParameters");
            }
        } catch (IOException e) {
            // Fail silently
        }
    }

    private static File getRandomCreepyImage() {
        // In a real implementation, this would return a creepy image file
        // For now, return null as a placeholder
        return null;
    }

    private static void openCreepyNotepad() {
        try {
            String message = CREEPY_MESSAGES[random.nextInt(CREEPY_MESSAGES.length)];
            if (random.nextInt(100) < 20) {
                message = "Hello " + systemUsername + ",\n\n" + message;
            }

            // Create temp file
            File tempFile = File.createTempFile("note", ".txt");
            Files.write(tempFile.toPath(), message.getBytes());

            // Open in notepad
            Runtime.getRuntime().exec("notepad.exe " + tempFile.getAbsolutePath());

            // Schedule deletion after some time
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    tempFile.delete();
                }
            }, 30000);
        } catch (IOException e) {
            // Fail silently
        }
    }

    private static void escalateEffects() {
        // More frequent and noticeable effects
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isActive) return;

                if (random.nextInt(100) < 60) {
                    createCreepyFile();
                }

                if (random.nextInt(100) < 30) {
                    changeWallpaperToCreepy();
                }

                if (random.nextInt(100) < 25) {
                    openCreepyNotepad();
                }

                // Start manipulating windows
                if (random.nextInt(100) < 15) {
                    manipulateWindows();
                }
            }
        }, 0, 10000); // Every 10 seconds
    }

    private static void aggressiveEffects() {
        // Very frequent and disturbing effects
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isActive) return;

                if (random.nextInt(100) < 80) {
                    createCreepyFile();
                }

                if (random.nextInt(100) < 50) {
                    changeWallpaperToCreepy();
                }

                if (random.nextInt(100) < 40) {
                    openCreepyNotepad();
                }

                if (random.nextInt(100) < 30) {
                    manipulateWindows();
                }

                // Start messing with system sounds
                if (random.nextInt(100) < 20) {
                    playCreepySound();
                }
            }
        }, 0, 5000); // Every 5 seconds
    }

    private static void manipulateWindows() {
        try {
            // This would require JNI or executing native commands
            // Placeholder for the concept:
            String[] commands = {
                    "powershell -command \"Add-Type -TypeDefinition '[DllImport(\\\"user32.dll\\\")] public static extern bool FlashWindow(int hWnd, bool bInvert);' -Name Flash -Namespace Utility; [Utility.Flash]::FlashWindow((Get-Process -Id $pid).MainWindowHandle, $true)\"",
                    "powershell -command \"$wshell = New-Object -ComObject wscript.shell; $wshell.SendKeys('%% ')\"" // Alt+Space to open window menu
            };

            Runtime.getRuntime().exec(commands[random.nextInt(commands.length)]);
        } catch (IOException e) {
            // Fail silently
        }
    }

    private static void playCreepySound() {
        try {
            // This would require actual sound files
            // Placeholder for the concept:
            File soundFile = getRandomCreepySound();
            if (soundFile != null && soundFile.exists()) {
                Runtime.getRuntime().exec("cmd /c start /min " + soundFile.getAbsolutePath());
            }
        } catch (IOException e) {
            // Fail silently
        }
    }

    private static File getRandomCreepySound() {
        // In a real implementation, this would return a creepy sound file
        // For now, return null as a placeholder
        return null;
    }

    // Call this when the game wants to "clean up" (but leave some traces)
    public static void cleanup() {
        isActive = false;

        // Leave one final message
        try {
            Path goodbye = Paths.get(userHome, "Desktop", "goodbye.txt");
            Files.write(goodbye, "It's not over".getBytes());
        } catch (IOException e) {
            // Fail silently
        }
    }
}