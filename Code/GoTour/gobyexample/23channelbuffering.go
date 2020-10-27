package main

import "fmt"

func main() {
	msgs := make(chan string, 3)

	msgs <- "channel"
	msgs <- "buffer"
	msgs <- "ing"

	fmt.Println(<-msgs)
	fmt.Println(<-msgs)
	fmt.Println(<-msgs)
}

/*
channel
buffer
ing
*/
