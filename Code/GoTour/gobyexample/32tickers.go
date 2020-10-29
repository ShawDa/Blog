package main

import (
	"fmt"
	"time"
)

func main() {
	ticker := time.NewTicker(500 * time.Millisecond)
	done := make(chan bool)

	go func() {
		for {
			select {
			case <-done:
				fmt.Println("done")
				return
			case t := <-ticker.C:
				fmt.Println("Tick at", t)
			}
		}
	}()

	time.Sleep(2222 * time.Millisecond)
	done <- true
	time.Sleep(100 * time.Millisecond)
	ticker.Stop()
	fmt.Println("Ticker stopped")
}

/*
Tick at 2020-10-30 00:20:34.2142268 +0800 CST m=+0.509650701
Tick at 2020-10-30 00:20:34.7152275 +0800 CST m=+1.010651401
Tick at 2020-10-30 00:20:35.2169919 +0800 CST m=+1.512415801
Tick at 2020-10-30 00:20:35.7190375 +0800 CST m=+2.014461401
done
Ticker stopped
*/
