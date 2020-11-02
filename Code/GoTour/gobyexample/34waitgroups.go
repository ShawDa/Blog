package main

import (
	"fmt"
	"sync"
	"time"
)

func main() {
	var group sync.WaitGroup
	// Launch several goroutines and increment the WaitGroup counter for each.
	for i := 1; i < 6; i++ {
		group.Add(1)
		go waitWorker(i, &group)
	}

	group.Wait() // Block until the WaitGroup counter goes back to 0; all the workers notified they’re done.
}

/*
2020-11-02 23:49:30.3511646 +0800 CST m=+0.006980501, Worker 2 starting
2020-11-02 23:49:30.3511646 +0800 CST m=+0.006980501, Worker 3 starting
2020-11-02 23:49:30.3511646 +0800 CST m=+0.006980501, Worker 4 starting
2020-11-02 23:49:30.3511646 +0800 CST m=+0.006980501, Worker 5 starting
2020-11-02 23:49:30.3511646 +0800 CST m=+0.006980501, Worker 1 starting
2020-11-02 23:49:31.3845936 +0800 CST m=+1.040409501, Worker 4 done
2020-11-02 23:49:31.3844895 +0800 CST m=+1.040305401, Worker 1 done
2020-11-02 23:49:31.3845936 +0800 CST m=+1.040409501, Worker 2 done
2020-11-02 23:49:31.3845936 +0800 CST m=+1.040409501, Worker 5 done
2020-11-02 23:49:31.3845936 +0800 CST m=+1.040409501, Worker 3 done
*/
func waitWorker(id int, group *sync.WaitGroup) {
	defer group.Done()
	fmt.Printf("%s, Worker %d starting\n", time.Now(), id)
	time.Sleep(time.Second)
	fmt.Printf("%s, Worker %d done\n", time.Now(), id)
}
