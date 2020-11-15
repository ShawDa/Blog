package main

func main() {
	
}

/*
122. 买卖股票的最佳时机 II
https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-ii/
 */
func maxProfit(prices []int) int {
	maxProfit := 0
	for i := 0; i < len(prices) - 1; i++ {
		diff := prices[i + 1] - prices[i]
		if diff > 0 {
			maxProfit += diff
		}
	}
	return maxProfit
}
