package main

import "fmt"

func main() {
	intsChannel := make(chan int, 3)
	// 0xc000078080 0xc000006028
	fmt.Printf("%v %p\n", intsChannel, &intsChannel)

	intsChannel <- 100
	intsChannel <- 1000
	intsChannel <- 10000
	// intsChannel <- 100000  // fatal error: all goroutines are asleep - deadlock!

	num1 := <-intsChannel
	num2 := <-intsChannel
	num3 := <-intsChannel
	fmt.Printf("%v %v %v\n", num1, num2, num3) // 100 1000 10000
	// <-intsChannel  // fatal error: all goroutines are asleep - deadlock!

	allTypes := make(chan interface{}, 10)
	allTypes <- 100
	allTypes <- "cat"
	f := 3.14
	allTypes <- &f
	fmt.Printf("%v %v %v\n", <-allTypes, <-allTypes, <-allTypes) // 100 cat 0xc00000a0c0

	closeChannel := make(chan int, 3)
	closeChannel <- 1
	closeChannel <- 2
	close(closeChannel)
	// closeChannel <- 3  // panic: send on closed channel
	fmt.Printf("%v %v\n", <-closeChannel, <-closeChannel) // 1 2

	rangeChannel := make(chan int, 100)
	for i := 0; i < 100; i++ {
		rangeChannel <- i * i
	}
	// close 之前不能遍历，goroutine 1 [chan receive]:
	//for channel := range rangeChannel {
	//	fmt.Println(channel)
	//}
	close(rangeChannel)
	for channel := range rangeChannel {
		fmt.Println(channel)
	}

	onlyWrite := make(chan<- int, 3)
	onlyWrite <- 10

	onlyRead := make(<-chan int, 3)
	_, _ = <-onlyRead
}
