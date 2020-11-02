package main

import (
	"fmt"
	"time"
)

func main() {
	const numJobs = 5
	jobs := make(chan int, numJobs)
	results := make(chan int, numJobs)

	for i := 1; i < 4; i++ {
		go thisWorker(i, jobs, results)
	}

	for i := 1; i < numJobs+1; i++ {
		jobs <- i
	}
	close(jobs)

	for i := 1; i < numJobs+1; i++ {
		fmt.Println(<-results)
	}
}

/*
2020-11-02 23:41:02.5138058 +0800 CST m=+0.008976601 worker 3 started job 1
2020-11-02 23:41:02.5227795 +0800 CST m=+0.017950301 worker 2 started job 3
2020-11-02 23:41:02.5227795 +0800 CST m=+0.017950301 worker 1 started job 2
2020-11-02 23:41:03.6174124 +0800 CST m=+1.112583201 worker 1 finished job 2
2020-11-02 23:41:03.6174124 +0800 CST m=+1.112583201 worker 1 started job 4
4
2020-11-02 23:41:03.6174124 +0800 CST m=+1.112583201 worker 2 finished job 3
2020-11-02 23:41:03.6174124 +0800 CST m=+1.112583201 worker 2 started job 5
6
2020-11-02 23:41:03.6174124 +0800 CST m=+1.112583201 worker 3 finished job 1
2
2020-11-02 23:41:04.619149 +0800 CST m=+2.114319801 worker 2 finished job 5
2020-11-02 23:41:04.6196732 +0800 CST m=+2.114844001 worker 1 finished job 4
10
8
*/
func thisWorker(id int, jobs <-chan int, results chan<- int) {
	for job := range jobs {
		fmt.Println(time.Now(), "worker", id, "started job", job)
		time.Sleep(time.Second)
		fmt.Println(time.Now(), "worker", id, "finished job", job)
		results <- 2 * job
	}
}
