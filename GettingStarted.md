# Getting Started

## Overview
This guide covers the setup required to participate in the AFABL Programmer Study. To begin, simply click the operating system you will be using for the study from the list below or click the "Quick Start" link for a quick instructional guide that is sutible for all operating systems and assumes some experience with installing programs and plugins in Intellij IDEA.

- [Microsoft Windows 7/8/8.1/10](#windows)
- [Mac OS X 10.8+](#mac)
- [Linux (Running GNOME or KDE)](#linux)
- [Quick Start](#quick)

## <a name="windows">Microsoft Windows 7/8/8.1/10</a>

### Contents
- [System Requirements](#windows_requirements)
- [Checking for the Java 8 JDK](#windows_java_check)
- [Installing the Java 8 JDK](#windows_java_install)
- [Checking for Intellij IDEA Community Edition](#windows_intellij_check)
- [Installing Intellij IDEA Community Edition](#windows_intellij_install)
- [Installing the Scala Language plugin for Intellij IDEA](#windows_scala_install)
- [Installing the Bunny plugin for Intellij IDEA](#windows_bunny_install)
- [Setting up the AFABL Study project in Intellij IDEA](#windows_project_setup)
- [Working on the AFABL Study project](#windows_project_working)
- [Cleaning up after submitting results](#windows_cleanup)

### <a name="windows_requirements">System Requirements</a>

#### Hardware Requirements
- 1 GB RAM minimum, 2 GB RAM recommended
- 300 MB hard disk space + at least 1 GB for caches
- 1024x768 minimum screen resolution

#### System Requirements
- Java Development Kit 1.8+

### <a name="windows_java_check">Checking for the Java 8 JDK</a>

#### Using cmd

1. Open the command prompt
2. Run the command `javac -version` and `java -version`
3. Verify both show "`java-1.8.0_xxx`" (where xxx is a two or three digit number)
4. If the you see `'javac' is not recognized as an internal or external command,
operable program or batch file.` or `'java' is not recognized as an internal or external command,
operable program or batch file.` proceed to the next section, otherwise skip to [Checking for Intellij IDEA Community Edition](#windows_intellij_check).

### <a name="windows_java_install">Installing the Java 8 JDK</a>

[Click here for intallation instructions](https://docs.oracle.com/javase/8/docs/technotes/guides/install/windows_jdk_install.html#CHDEBCCJ).

### <a name="windows_intellij_check">Checking for Intellij IDEA Community Edition</a>

1. Check if Intellij IDEA Community Edition is installed. 
2. If not, proceed to [Installing Intellij IDEA Community Edition](#windows_intellij_install).
3. Otherwise, open Intellij IDEA Community Edition and find the version number by clicking <b>Help</b>, then clicking <b>About</b>. 
4. Find the build number and ensure it is at least "`Build #IC-162.2032.8`". 
5. If not, please click <b>Help</b>, then <b>Check for Updates</b> and upate to the latest version of Intellij IDEA.
6. Otherwise, skip to [Installing the Scala Language plugin for Intellij IDEA](#windows_scala_install).

### <a name="windows_intellij_install">Installing Intellij IDEA Community Edition</a>

1. Download Intellij IDEA Community Edition from the [JetBrains Website](https://www.jetbrains.com/idea/download/#section=windows).
2. Run the `ideaIC or ideaIU-*.exe` file that starts the Installation Wizard.
3. Follow all steps suggested by the wizard. 
4. Launch Intellij IDEA for the first time to configure it. If prompted with the option to install the Scala plugin, please do so.

### <a name="windows_scala_install">Installing the Scala Language plugin for Intellij IDEA</a>

1. Open Intellij IDEA Community Edition.
2. Open <b>Settings</b> by clicking <b>File</b> followed by <b>Settings</b> or if at the Welcome menu, click <b>Configure</b> followed by <b>Settings</b>.
3. In the left-hand pane, select Plugins.
4. At the bottom of the right pane click <b>Browse repositories...</b>
5. In the search bar at the top of the Browse Repositories screen, type "Scala".
6. Scroll through the list of plugins until you find one named "Scala" and click it.
7. In the right pane, verify that it is a "LANGUAGES" plugin and the vendor of the plugin is JetBrains
8. Click the green <b>Install</b> button. If there in no <b>Install</b> button, it means the plugin is already installed and you can proceed to [Installing the Bunny plugin for Intellij IDEA](#windows_bunny_install).
9. After the plugin is installed, restart Intellij IDEA (the Install button will be replaced by a button that does this).

### <a name="windows_bunny_install">Installing the Bunny plugin for Intellij IDEA</a>

1. Download the Bunny plugin from [it's repoistory](https://github.com/kjcartledge/Bunny/releases/download/v1.0/Bunny.jar).
2. Open Intellij IDEA Community Edition.
3. Open <b>Settings</b> by clicking <b>File</b> followed by <b>Settings</b>.
4. In the left-hand pane, select Plugins.
5. At the bottom of the right pane click <b>Install plugin from disk...</b>
6. Find the downloaded `Bunny.jar`, select it, and click <b>OK</b>.
7. Click the green <b>Install</b> button. If there in no <b>Install</b> button, it means the plugin is already installed and you can proceed to [Setting up the AFABL Study project in Intellij IDEA](#windows_project_setup).
8. After the plugin is installed, restart Intellij IDEA (the Install button will be replaced by a button that does this).

### <a name="windows_project_setup">Setting up the AFABL Study project in Intellij IDEA</a>

1. Find the `afabl_study.zip` that was given to you after you signed up at [bunny.afabl.org](http://bunny.afabl.org).
2. If you do not have this, just sign up at [bunny.afabl.org](http://bunny.afabl.org), it's okay if you have already done this.
3. Extract the zip to a directory of your choosing. This will be the working directory for the project.
4. Open Intellij IDEA Community Edition.
5. Click <b>File</b> followed by <b>Open</b> or If at the Welcome menu, click <b>Open</b>
6. Navitage to the directory that you extracted the zip file to, click it, and click <b>OK</b>.

### <a name="windows_project_working">Working on the AFABL Study project</a>

1. Open the afabl_study project.
2. Double-click the `bunny.scala` file in the `src` directory.
3. Read the Welcome prompt and click okay to start.
4. Complete the assignment in `bunny.scala` per the provided instructions.
5. When you are finished click <b>Tools</b> followed by <b>Bunny</b> then <b>Submit</b> to complete the study and submit your results.

### <a name="windows_project_finish">Finishing the AFABL Study project</a>

1. When you are finished click <b>Tools</b> followed by <b>Bunny</b> then <b>Submit</b> to complete the study and submit your results.
2. If this fails, you can use the <b>Export Results</b> option instead of <b>Submit</b> to generate a text file that can be submited at a later time.

### <a name="windows_cleanup">Cleaning up after submitting results</a>

After your results have been submitted, you can remove any and all software installed for this study.

## <a name="mac">Mac OS X 10.8+</a>

### Contents
- [System Requirements](#mac_requirements)
- [Checking for the Java 8 JDK](#mac_java_check)
- [Installing the Java 8 JDK](#mac_java_install)
- [Checking for Intellij IDEA Community Edition](#mac_intellij_check)
- [Installing Intellij IDEA Community Edition](#mac_intellij_install)
- [Installing the Scala Language plugin for Intellij IDEA](#mac_scala_install)
- [Installing the Bunny plugin for Intellij IDEA](#mac_bunny_install)
- [Setting up the AFABL Study project in Intellij IDEA](#mac_project_setup)
- [Working on the AFABL Study project](#mac_project_working)
- [Cleaning up after submitting results](#mac_cleanup)

### <a name="mac_requirements">System Requirements</a>

#### Hardware Requirements
- 1 GB RAM minimum, 2 GB RAM recommended
- 300 MB hard disk space + at least 1 GB for caches
- 1024x768 minimum screen resolution

#### System Requirements
- Only 64-bit OS X is supported.
- Java Development Kit 1.8+

### <a name="mac_java_check">Checking for the Java 8 JDK</a>

#### Using Terminal

1. Open Terminal
2. Run the command `javac -version` and `java -version`
3. Verify both show "`java-1.8.0_xxx`" (where xxx is a two or three digit number)
4. If the you see `-bash: javac: command not found` or `-bash: java: command not found` proceed to the next section, otherwise skip to [Checking for Intellij IDEA Community Edition](#mac_intellij_check).

### <a name="mac_java_install">Installing the Java 8 JDK</a>

[Click here for intallation instructions](https://docs.oracle.com/javase/8/docs/technotes/guides/install/mac_jdk.html#CHDBADCG).

### <a name="mac_intellij_check">Checking for Intellij IDEA Community Edition</a>

1. Check if Intellij IDEA Community Edition is installed. 
2. If not, proceed to [Installing Intellij IDEA Community Edition](#mac_intellij_install).
3. Otherwise, open Intellij IDEA Community Edition and find the version number by clicking <b>Intellij IDEA</b>, then clicking <b>About Intellij IDEA</b>. 
4. Find the build number and ensure it is at least "`Build #IC-162.2032.8`". 
5. If not, please click <b>Intellij IDEA</b>, then <b>Check for Updates...</b> and upate to the latest version of Intellij IDEA.
6. Otherwise, skip to [Installing the Scala Language plugin for Intellij IDEA](#mac_scala_install).

### <a name="mac_intellij_install">Installing Intellij IDEA Community Edition</a>


1. Download Intellij IDEA Community Edition from the [JetBrains Website](https://www.jetbrains.com/idea/download/#section=macos).
2. Double-click the downloaded `ideaIC or ideaIU-*.dmg` OS X Disk Image file to mount it.
3. Copy IntelliJ IDEA to your <b>Applications</b> folder.
4. Launch Intellij IDEA for the first time to configure it. If prompted with the option to install the Scala plugin, please do so.

### <a name="mac_scala_install">Installing the Scala Language plugin for Intellij IDEA</a>

1. Open Intellij IDEA Community Edition.
2. Open <b>Preferences</b> by clicking <b>Intellij IDEA</b> followed by <b>Preferences</b> or if at the Welcome menu, click <b>Configure</b> followed by <b>Preferences</b>.
3. In the left-hand pane, select Plugins.
4. At the bottom of the right pane click <b>Browse repositories...</b>
5. In the search bar at the top of the Browse Repositories screen, type "Scala".
6. Scroll through the list of plugins until you find one named "Scala" and click it.
7. In the right pane, verify that it is a "LANGUAGES" plugin and the vendor of the plugin is JetBrains
8. Click the green <b>Install</b> button. If there in no <b>Install</b> button, it means the plugin is already installed and you can proceed to [Installing the Bunny plugin for Intellij IDEA](#mac_bunny_install).
9. After the plugin is installed, restart Intellij IDEA (the Install button will be replaced by a button that does this).

### <a name="mac_bunny_install">Installing the Bunny plugin for Intellij IDEA</a>

1. Download the Bunny plugin from [it's repoistory](https://github.com/kjcartledge/Bunny/releases/download/v1.0/Bunny.jar).
2. Open Intellij IDEA Community Edition.
3. Open <b>Preferences</b> by clicking <b>Intellij IDEA</b> followed by <b>Preferences</b>.
4. In the left-hand pane, select Plugins.
5. At the bottom of the right pane click <b>Install plugin from disk...</b>
6. Find the downloaded `Bunny.jar`, select it, and click <b>OK</b>.
7. Click the green <b>Install</b> button. If there in no <b>Install</b> button, it means the plugin is already installed and you can proceed to [Setting up the AFABL Study project in Intellij IDEA](#mac_project_setup).
8. After the plugin is installed, restart Intellij IDEA (the Install button will be replaced by a button that does this).

### <a name="mac_project_setup">Setting up the AFABL Study project in Intellij IDEA</a>

1. Find the `afabl_study.zip` that was given to you after you signed up at [bunny.afabl.org](http://bunny.afabl.org).
2. If you do not have this, just sign up at [bunny.afabl.org](http://bunny.afabl.org), it's okay if you have already done this.
3. Extract the zip to a directory of your choosing. This will be the working directory for the project.
4. Open Intellij IDEA Community Edition.
5. Click <b>File</b> followed by <b>Open</b> or If at the Welcome menu, click <b>Open</b>
6. Navitage to the directory that you extracted the zip file to, click it, and click <b>OK</b>.

### <a name="mac_project_working">Working on the AFABL Study project</a>

1. Open the afabl_study project.
2. Double-click the `bunny.scala` file in the `src` directory.
3. Read the Welcome prompt and click okay to start.
4. Complete the assignment in `bunny.scala` per the provided instructions.

### <a name="mac_project_finish">Finishing the AFABL Study project</a>

1. When you are finished click <b>Tools</b> followed by <b>Bunny</b> then <b>Submit</b> to complete the study and submit your results.
2. If this fails, you can use the <b>Export Results</b> option instead of <b>Submit</b> to generate a text file that can be submited at a later time.

### <a name="mac_cleanup">Cleaning up after submitting results</a>

After your results have been submitted, you can remove any and all software installed for this study.

## <a name="linux">Linux (Running GNOME or KDE)</a>

### Contents
- [System Requirements](#linux_requirements)
- [Checking for the Java 8 JDK](#linux_java_check)
- [Installing the Java 8 JDK](#linux_java_install)
- [Checking for Intellij IDEA Community Edition](#linux_intellij_check)
- [Installing Intellij IDEA Community Edition](#linux_intellij_install)
- [Installing the Scala Language plugin for Intellij IDEA](#linux_scala_install)
- [Installing the Bunny plugin for Intellij IDEA](#linux_bunny_install)
- [Setting up the AFABL Study project in Intellij IDEA](#linux_project_setup)
- [Working on the AFABL Study project](#linux_project_working)
- [Cleaning up after submitting results](#linux_cleanup)

### <a name="linux_requirements">System Requirements</a>

#### Hardware Requirements
- 1 GB RAM minimum, 2 GB RAM recommended
- 300 MB hard disk space + at least 1 GB for caches
- 1024x768 minimum screen resolution

#### System Requirements
- OS Linux 64 bit
- KDE, GNOME or Unity DE desktop
- Java Development Kit 1.8+

### <a name="linux_java_check">Checking for the Java 8 JDK</a>

#### Using the command line

1. Open the command line
2. Run the command `javac -version` and `java -version`
3. Verify both show "`java-1.8.0_xxx`" (where xxx is a two or three digit number)
4. If the you see `javac: command not found` or `java: command not found` proceed to the next section, otherwise skip to [Checking for Intellij IDEA Community Edition](#linux_intellij_check).

### <a name="linux_java_install">Installing the Java 8 JDK</a>

[Click here for intallation instructions](https://docs.oracle.com/javase/8/docs/technotes/guides/install/linux_jdk.html#BJFGGEFG).

### <a name="linux_intellij_check">Checking for Intellij IDEA Community Edition</a>

1. Check if Intellij IDEA Community Edition is installed. 
2. If not, proceed to [Installing Intellij IDEA Community Edition](#linux_intellij_install).
3. Otherwise, open Intellij IDEA Community Edition and find the version number by clicking <b>Help</b>, then clicking <b>About</b>. 
4. Find the build number and ensure it is at least "`Build #IC-162.2032.8`". 
5. If not, please click <b>Help</b>, then <b>Check for Updates</b> and upate to the latest version of Intellij IDEA.
6. Otherwise, skip to [Installing the Scala Language plugin for Intellij IDEA](#linux_scala_install).

### <a name="linux_intellij_install">Installing Intellij IDEA Community Edition</a>

1. Download the `<ideaIC or ideaIU>-*.tar.gz` file from the [JetBrains Website](https://www.jetbrains.com/idea/download/#section=linux).
2. Unpack the `<ideaIC or ideaIU>-*.tar.gz` file to a different folder, if your current "Download" folder doesn't support file execution:
`tar xfz <ideaIC or ideaIU>-*.tar.gz <new_archive_folder>`. The recommended install location according to the filesystem hierarchy standard (FHS) is `/opt`. For example, it's possible to enter the following command: `sudo tar xf <ideaIC or ideaIU>-*.tar.gz -C /opt/`.
3. Switch to the bin directory:
cd `<new archive folder>/<ideaIC or ideaIU>-*/bin`. For example, `cd opt/<ideaIC or ideaIU>-*/bin`.
4. Run `idea.sh` from the `bin` subdirectory.

### <a name="linux_scala_install">Installing the Scala Language plugin for Intellij IDEA</a>

1. Open Intellij IDEA Community Edition.
2. Open <b>Settings</b> by clicking <b>File</b> followed by <b>Settings</b> or if at the Welcome menu, click <b>Configure</b> followed by <b>Settings</b>.
3. In the left-hand pane, select Plugins.
4. At the bottom of the right pane click <b>Browse repositories...</b>
5. In the search bar at the top of the Browse Repositories screen, type "Scala".
6. Scroll through the list of plugins until you find one named "Scala" and click it.
7. In the right pane, verify that it is a "LANGUAGES" plugin and the vendor of the plugin is JetBrains
8. Click the green <b>Install</b> button. If there in no <b>Install</b> button, it means the plugin is already installed and you can proceed to [Installing the Bunny plugin for Intellij IDEA](#linux_bunny_install).
9. After the plugin is installed, restart Intellij IDEA (the Install button will be replaced by a button that does this).

### <a name="linux_bunny_install">Installing the Bunny plugin for Intellij IDEA</a>

1. Download the Bunny plugin from [it's repoistory](https://github.com/kjcartledge/Bunny/releases/download/v1.0/Bunny.jar).
2. Open Intellij IDEA Community Edition.
3. Open <b>Settings</b> by clicking <b>File</b> followed by <b>Settings</b>.
4. In the left-hand pane, select Plugins.
5. At the bottom of the right pane click <b>Install plugin from disk...</b>
6. Find the downloaded `Bunny.jar`, select it, and click <b>OK</b>.
7. Click the green <b>Install</b> button. If there in no <b>Install</b> button, it means the plugin is already installed and you can proceed to [Setting up the AFABL Study project in Intellij IDEA](#linux_project_setup).
8. After the plugin is installed, restart Intellij IDEA (the Install button will be replaced by a button that does this).

### <a name="linux_project_setup">Setting up the AFABL Study project in Intellij IDEA</a>

1. Find the `afabl_study.zip` that was given to you after you signed up at [bunny.afabl.org](http://bunny.afabl.org).
2. If you do not have this, just sign up at [bunny.afabl.org](http://bunny.afabl.org), it's okay if you have already done this.
3. Extract the zip to a directory of your choosing. This will be the working directory for the project.
4. Open Intellij IDEA Community Edition.
5. Click <b>File</b> followed by <b>Open</b> or If at the Welcome menu, click <b>Open</b>
6. Navitage to the directory that you extracted the zip file to, click it, and click <b>OK</b>.

### <a name="linux_project_working">Working on the AFABL Study project</a>

1. Open the afabl_study project.
2. Double-click the `bunny.scala` file in the `src` directory.
3. Read the Welcome prompt and click okay to start.
4. Complete the assignment in `bunny.scala` per the provided instructions.

### <a name="linux_project_finish">Finishing the AFABL Study project</a>

1. When you are finished click <b>Tools</b> followed by <b>Bunny</b> then <b>Submit</b> to complete the study and submit your results.
2. If this fails, you can use the <b>Export Results</b> option instead of <b>Submit</b> to generate a text file that can be submited at a later time.

### <a name="linux_cleanup">Cleaning up after submitting results</a>

After your results have been submitted, you can remove any and all software installed for this study.

## <a name="quick">Quick Start</a>

### Contents
- [Setup](#quick_setup)
- [Working on the project](#quick_working)
- [Submitting the results](#quick_submit)
- [Cleaning up](#quick_cleanup)

### <a name="quick_setup">Setup</a>

#### Requirements
Ensure your system meets the following requirements:
- 1 GB RAM minimum, 2 GB RAM recommended
- 300 MB hard disk space + at least 1 GB for caches
- 1024x768 minimum screen resolution
- Mac OS X 10.8+ OR Microsoft Windows 10/8/7/Vista/2003/XP (incl.64-bit) OR Linux with GNOME or KDE desktop
- Java JDK version 1.8.0+
- Internet Access

#### Setting Up Intellij IDEA Community Edition
0. If you already have Intellij IDEA Community Edition installed, check that it is build version 162.2032 or higher, if so, skip to step 3. If not, update to the latest version or goto step 1.
1. Download the Intellij IDEA Community Edition from the [offical website](https://www.jetbrains.com/idea/download/#)
2. Install Intellij IDEA by following the install instructions for your operating system provided on the download page linked above.
3. Install the Scala Language plugin 

### <a name="quick_working">Working on the project</a>

1. Open the afabl_study project.
2. Double-click the bunny.scala file in the `src` directory.
3. Read the Welcome prompt and click okay to start.
4. Complete the assignment in `bunny.scala` per the provided instructions.

### <a name="quick_submit">Submitting the results</a>

1. When you are finished click <b>Tools</b> followed by <b>Bunny</b> then <b>Submit</b> to complete the study and submit your results.
2. If this fails, you can use the <b>Export Results</b> option instead of <b>Submit</b> to generate a text file that can be submited at a later time.

### <a name="quick_cleanup">Cleaning up</a>

After your results have been submitted, you can remove any and all software installed for this study.
