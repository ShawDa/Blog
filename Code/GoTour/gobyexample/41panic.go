package main

import (
	"fmt"
	"os"
)

// Running this program will cause it to panic,
// print an error message and goroutine traces, and exit with a non-zero status.
func main() {
	_, err := os.Create("/tmp/file")
	if err != nil {
		panic(err)
	}
	fmt.Println("Can we get here?")
}

/*
panic: open /tmp/file: The system cannot find the path specified.

goroutine 1 [running]:
main.main()
	E:/Program/ideaprojects/CodingRecords/Code/GoTour/gobyexample/41panic.go:13 +0xdb

Process finished with exit code 2
*/
