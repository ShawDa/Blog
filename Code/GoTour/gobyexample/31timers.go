package main

import (
	"fmt"
	"time"
)

func main() {
	timer1 := time.NewTimer(2 * time.Second)
	fmt.Println(<-timer1.C) // blocks on the timer’s channel C until it sends a value indicating that the timer fired.
	fmt.Println("Time 1 fired")

	timer2 := time.NewTimer(time.Second)
	go func() {
		fmt.Println(<-timer2.C)
		fmt.Println("Timer 2 fired")
	}()
	stop2 := timer2.Stop()
	if stop2 {
		fmt.Println(time.Now())
		fmt.Println("Timer 2 stopped")
	}

	time.Sleep(2 * time.Second)
	fmt.Println(time.Now())
}

/*
2020-10-30 00:14:27.7869783 +0800 CST m=+2.007818101
Time 1 fired
2020-10-30 00:14:27.8176552 +0800 CST m=+2.038495001
Timer 2 stopped
2020-10-30 00:14:29.8183688 +0800 CST m=+4.039208601
*/
