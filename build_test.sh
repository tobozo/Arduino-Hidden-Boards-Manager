#!/bin/bash

# Arduino IDE tools folder destination path
arduino_tools_folder=~/Arduino/tools/HiddenBoardsManager/tool
# Arduino IDE mime info file (will look for an "Exec" entry containing the full path to arduino ide executable
mime_type_file="arduino-arduinoide.desktop"
# Relative path to compiled jar
jar_file="build/HiddenBoardsManager.jar"

# 1) build the project
. .github/scripts/build_jar.sh $jar_file
[ $? -eq 0 ] || die "Unable to build jar"

if [ "$1" != "nozip" ]; then
  # 1.1) create packages

  .github/scripts/create_arduino_package.sh $jar_file
  [ $? -eq 0 ] || die "Arduino Package creation failed"

fi


# 2) copy the jar to Arduino IDE tools folder
mkdir -p "$arduino_tools_folder"
cp $jar_file "$arduino_tools_folder/HiddenBoardsManager.jar" || die "Unable to copy jar to Arduino IDE tools folder: $arduino_tools_folder"
echo "Copied jar file to Arduino IDE tools folder: $arduino_tools_folder"

# 4) find Arduino IDE binary location (assuming KDE Plasma or Gnome desktop have a mime file)
mimefile=`find ~/.local/ -name $mime_type_file`
if [[ "$mimefile" != "" ]]; then
  cmd=`grep 'Exec=' $mimefile`
  cmd=${cmd#*=}
  if [[ "$cmd" != "" ]]; then
    # 5) launch Arduino IDE
    echo "Launching Arduino IDE at $cmd"
    if [[ ! -f "$cmd" ]]; then
      echo "FILE DOES NOT EXIST (mimefile=$mimefile)"
    else
      $cmd
    fi
  else
    echo "Mime file has no valid Exec value"
  fi
else
  echo "Mime file not found for ino files"
fi

