package main

import (
	"fmt"
)

func main() {
	var liczba int
	_, err := fmt.Scanf("Podaj liczbe: %d", &liczba)
	if err != nil {
		panic(err)
	}

	var size int16
	for true {
		var dzielnik = 2

		if liczba%dzielnik != 0 {
			dzielnik++
		} else {

		}
	}
	dzielniki := [size]int[]

}
