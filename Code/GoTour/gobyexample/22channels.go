package main

import "fmt"

func main() {
	msg := make(chan string) // Create a new channel

	go func() { msg <- "ping" }() // Send a value into a channel from new goroutine

	thisMsg := <-msg // Receives a value from the channel
	fmt.Println(thisMsg)
}
