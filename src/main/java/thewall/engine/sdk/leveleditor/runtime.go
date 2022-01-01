package main

import (
	"fmt"
	"log"
	"os/exec"
)

func main() {
	cmd := exec.Command("gulp", "serv.dev")
	if err := cmd.Run(); err != nil {
		log.Fatal(err)
	}

	fmt.Println("Starting JTEEditor Runtime")

	updateCmd := exec.Command("update.exe")

	err := updateCmd.Run()

	if err != nil {
		fmt.Println("Cannot start update check", err)
	}

	cmdJava := exec.Command("bin\\java.exe -jar JTEEditor.jar")

	errJava := cmdJava.Run()

	if errJava == nil {
		log.Fatal("Cannot start Editor", errJava)
	}
}
