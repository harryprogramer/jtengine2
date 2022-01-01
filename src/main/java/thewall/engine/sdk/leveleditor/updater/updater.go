package main

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"os"
	"path/filepath"
)

var VERSION = "JTEUpdater 1.0"

type UpdateFeedback struct {
	Update bool   `json:"update"`
	Path   string `json:"path"`
}
type UpdateRemoveChanges struct {
	Path string `json:"path"`
}

type UpdateAddChanges struct {
	Path       string `json:"path"`
	SourcePath string `json:"source_path"`
}

type UpdateReplaceChanges struct {
	Path       string `json:"path"`
	SourcePath string `json:"source_path"`
}

type UpdateChanges struct {
	RemoveChanges  []UpdateRemoveChanges  `json:"remove"`
	AddChanges     []UpdateAddChanges     `json:"add"`
	ReplaceChanges []UpdateReplaceChanges `json:"replace"`
}

func isUpdateNeeded() (bool, string) {
	jsonFile, err := os.Open("update/update.json")
	if err != nil {
		fmt.Printf("Cannot open update feedback, %s\n", err)
		return false, ""
	}

	defer jsonFile.Close()

	byteValue, err := ioutil.ReadAll(jsonFile)
	if err != nil {
		fmt.Printf("Cannot read update feedback %s\n", err)
		return false, ""
	}

	var update UpdateFeedback

	err = json.Unmarshal(byteValue, &update)
	if err != nil {
		fmt.Printf("Cannot unmarshall update feedback, %s\n", err.Error())
		return false, ""
	}

	return update.Update, update.Path

}

func listAllFiles(path string) []string {
	var files []string

	err := filepath.Walk(path, func(path string, info os.FileInfo, err error) error {
		files = append(files, path)
		return nil
	})
	if err != nil {
		panic(err)
	}

	return files
}

func update(updatePath string) {
	changes := parseChanges(fmt.Sprintf("%s/changes.json", updatePath))

	if len(changes.AddChanges) != 0 {
		for i, s := range changes.AddChanges {
			fmt.Printf("Creating file [%s] to [%s], %d left\n", s.SourcePath, s.Path, i)
			err := os.Rename(fmt.Sprintf("%s/%s", updatePath, s.SourcePath), fmt.Sprintf("%s/%s", updatePath, s.Path))
			if err != nil {
				fmt.Printf("Cannot move file [%s] to [%s], %s\n", s.SourcePath, s.Path, err)
			}
		}
	}

	if len(changes.RemoveChanges) != 0 {
		for i, s := range changes.RemoveChanges {
			fmt.Printf("Deleting file [%s], %d left\n", s.Path, i)
			err := os.Remove(s.Path)
			if err != nil {
				fmt.Printf("Cannot remove file [%s], [%s]\n", s.Path, err)
			}
		}
	}

	if len(changes.ReplaceChanges) != 0 {
		for i, s := range changes.ReplaceChanges {
			fmt.Printf("Copying file from [%s] to [%s], %d left\n", s.SourcePath, s.Path, i)
			err := os.Remove(s.Path)
			if err != nil {
				fmt.Printf("Cannot copy file from [%s] to [%s], %s\n", s.SourcePath, s.Path, err)
				continue
			}
			moveErr := os.Rename(s.SourcePath, s.Path)
			if moveErr != nil {
				fmt.Printf("Cannot copy file from [%s] to [%s], %s\n", s.SourcePath, s.Path, moveErr)
			}
		}
	}

	fmt.Printf("Update done!\n")
	os.Exit(0)
}

func parseChanges(path string) UpdateChanges {
	jsonFile, err := os.Open(path)
	if err != nil {
		fmt.Printf("Cannot open update feedback, %s\n", err)
	}

	defer jsonFile.Close()

	byteValue, err := ioutil.ReadAll(jsonFile)
	if err != nil {
		fmt.Printf("Cannot read update feedback %s\n", err)
	}

	var update UpdateChanges

	err = json.Unmarshal(byteValue, &update)
	if err != nil {
		fmt.Printf("Cannot unmarshall update changes feedback, %s\n", err.Error())
	}

	return update
}

func main() {
	path, err := os.Getwd()
	if err != nil {
		panic(fmt.Sprintf("Cannot get CWD, %s\n", err.Error()))
	}
	fmt.Printf("Starting %s, [%s]\n", VERSION, path)

	isUpdate, path := isUpdateNeeded()

	if isUpdate {
		update(path)
	} else {
		fmt.Println("Version is up to date!")
	}
}
