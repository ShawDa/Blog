package main

import (
	"fmt"
	"time"
)

func main() {
	done := make(chan bool, 1)
	go worker(done)

	fmt.Println(<-done)
}

/*
working...done
true
*/
func worker(done chan bool) {
	fmt.Print("working...")
	time.Sleep(time.Second)
	fmt.Println("done")
	time.Sleep(time.Second)
	done <- true
}
