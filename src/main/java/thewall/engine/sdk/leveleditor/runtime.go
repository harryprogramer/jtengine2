package main

import (
	"encoding/json"
	"errors"
	"fmt"
	"io/ioutil"
	"log"
	"os"
	"os/exec"
	"path/filepath"
)

type RuntimeConfig struct {
	Executable string `json:"executable"`
	Binaries   string `json:"binaries"`
}

func Start(args ...string) (p *os.Process, err error) {
	if args[0], err = exec.LookPath(args[0]); err == nil {
		var procAttr os.ProcAttr
		procAttr.Files = []*os.File{os.Stdin,
			os.Stdout, os.Stderr}
		p, err := os.StartProcess(args[0], args, &procAttr)
		if err == nil {
			return p, nil
		}
	}
	return nil, err
}

func WalkMatch(root, pattern string) ([]string, error) {
	var matches []string
	err := filepath.Walk(root, func(path string, info os.FileInfo, err error) error {
		if err != nil {
			return err
		}
		if info.IsDir() {
			return nil
		}
		if matched, err := filepath.Match(pattern, filepath.Base(path)); err != nil {
			return err
		} else if matched {
			matches = append(matches, path)
		}
		return nil
	})
	if err != nil {
		return nil, err
	}
	return matches, nil
}

func startEditor() {
	log.Println("[Launcher Runtime] Starting JTEEditor Runtime")

	if _, err := os.Stat("runtime.json"); errors.Is(err, os.ErrNotExist) {
		log.Println("[Launcher Runtime] Runtime config not found, creating default")
		f, err := os.OpenFile("runtime.json", os.O_CREATE|os.O_WRONLY, 0644)
		if err != nil {
			log.Println("Cannot create default settings")
			panic(err)
		}
		files, err := WalkMatch("", "*.jar")
		if err != nil {
			_, _ = f.WriteString(fmt.Sprintf("{\"executable\": \"%s\", \"binaries\": \"bin\"})", "editor.jar"))
		} else {
			_, _ = f.WriteString(fmt.Sprintf("{\"executable\": \"%s\", \"binaries\": \"bin\"})", files[0]))
		}
	}

	jsonFile, err := os.Open("runtime.json")
	if err != nil {
		fmt.Printf("[Launcher Runtime] Cannot open runtime config, %s\n", err)
	}

	byteValue, err := ioutil.ReadAll(jsonFile)
	if err != nil {
		fmt.Printf("[Launcher Runtime] Cannot read runtime config %s\n", err)
	}

	var config RuntimeConfig

	err = json.Unmarshal(byteValue, &config)
	if err != nil {
		log.Printf("[Launcher Runtime] Cannot unmarshall runtime config, %s\n", err.Error())
	}

	err = jsonFile.Close()
	if err != nil {
		panic(err)
	}

	log.Printf("[Launcher Runtime] Editor file: [%s]\n", config.Executable)

	log.Println("[Launcher Runtime] Checking updates...")

	procUpdate, errUpdate := Start("update.exe")
	if errUpdate != nil {
		log.Fatalf("[Launcher Runtime] Cannot check updates %s", errUpdate)
	} else {
		updateResult, _ := procUpdate.Wait()
		if updateResult.ExitCode() != 0 {
			log.Printf("[Launcher Runtime] Update check failed, returned code [%d]\n", updateResult.ExitCode())
		}
	}

	log.Println("[Launcher Runtime] Starting Editor...")

	proc, err := Start(fmt.Sprintf("%s\\java.exe", config.Binaries), "-jar", config.Executable)
	if err != nil {
		log.Fatalf(fmt.Sprintf("[Launcher Runtime] Cannot start executable Editor [%s]", err))
	}
	editorResult, _ := proc.Wait()

	if editorResult.ExitCode() == 15 {
		log.Println("[Launcher Runtime] Restart code returned from editor, restarting...")
		startEditor()
		return
	}

	if editorResult.ExitCode() != 0 && editorResult.ExitCode() != 130 {
		log.Printf("[Launcher Runtime] Editor start fault, returned code [%d]\n", editorResult.ExitCode())
		_, _ = fmt.Scanf(">")
	} else {
		os.Exit(0)
	}
}

func main() {
	startEditor()
}
