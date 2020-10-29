package main

import "fmt"

func main() {
	jobs := make(chan int, 5)
	done := make(chan bool)

	go func() {
		for {
			i, more := <-jobs
			if more {
				fmt.Println("received job", i)
			} else {
				fmt.Println("received all jobs")
				done <- true
				return
			}
		}
	}()

	for i := 1; i < 4; i++ {
		jobs <- i
		fmt.Println("sent job", i)
	}
	close(jobs)
	fmt.Println("sent all jobs")

	fmt.Println(<-done)
}

/*
sent job 1
received job 1
received job 2
sent job 2
sent job 3
sent all jobs
received job 3
received all jobs
true
*/
