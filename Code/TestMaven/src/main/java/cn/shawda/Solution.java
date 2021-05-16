package cn.shawda;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

class Solution {
    public static void main(String[] args) {
        System.out.println(Arrays.toString(new Solution().processQueries(new int[]{7,5,5,8,3}, 8)));
        System.out.println(new Solution().halvesAreAlike("AbCdEfGh"));
        System.out.println(new Solution().eatenApples(new int[]{3,0,0,0,0,10}, new int[]{3,0,0,0,0,10}));
        System.out.println(Arrays.toString(new Solution().findBall(new int[][]{{1, 1, 1, -1, -1}, {1, 1, 1, -1, -1}, {-1, -1, -1, 1, 1}, {1, 1, 1, 1, -1}, {-1, -1, -1, -1, -1}})));
        System.out.println(new Solution().minDays(new int[]{1,10,2,9,3,8,4,7,5,6}, 4, 2));
    }

    public int maxSumMinProduct(int[] nums) {
        int length = nums.length;
        long[] sum = new long[length];
        sum[0] = nums[0];
        for (int i = 1; i < length; i++) {
            sum[i] = sum[i - 1] + nums[i];
        }
        long[] res = new long[length];
        for (int i = 0; i < length; i++) {
            int num = nums[i];
            int left = i;
            while (left >= 0) {
                if (nums[left] < num) {
                    break;
                }
                left--;
            }
            int right = i;
            while (right < length) {
                if (nums[right] < num) {
                    break;
                }
                right++;
            }
            res[i] = num * (left == -1 ? sum[right - 1] : sum[right - 1] - sum[left]);
        }

        return (int) (LongStream.of(res).max().orElse(0) % 1000000007);
    }

    public int maxSumMinProductTimeOut(int[] nums) {
        int length = nums.length;
        long[] res = new long[length];
        for (int i = 0; i < length; i++) {
            long min = Long.MAX_VALUE;
            long sum = 0;
            for (int j = i; j < length; j++) {
                min = Math.min(min, nums[j]);
                sum += nums[j];
                res[i] = Math.max(min * sum, res[i]);
            }
        }
        return (int) (LongStream.of(res).max().orElse(0) % 1000000007);
    }

    public int maxDistance(int[] nums1, int[] nums2) {
        int res = 0;
        int left = 0;
        while (left < nums1.length) {
            int start = left + res;
            if (start >= nums2.length) {
                break;
            }
            while (start < nums2.length && nums1[left] <= nums2[start]) {
                start++;
            }
            res = Math.max(--start - left++, res);
        }
        return res;
    }

    public int maximumPopulation(int[][] logs) {
        int[] counts = new int[100];
        for (int[] log : logs) {
            for (int i = log[0]; i < log[1]; i++) {
                counts[i - 1950]++;
            }
        }
        int indexOfMax = 0;
        int max = 0;
        for (int i = 0; i < 100; i++) {
            if (counts[i] > max) {
                max = counts[i];
                indexOfMax = i;
            }
        }
        return 1950 + indexOfMax;
    }

    public int minDays(int[] bloomDay, int m, int k) {
        int length = bloomDay.length;
        if (m * k > length) {
            return -1;
        }
        int min = IntStream.of(bloomDay).min().orElse(1);
        int max = IntStream.of(bloomDay).max().orElse(1);
        while (min < max) {
            int mid = min + (max - min) / 2;
            if (isDayOk(bloomDay, m, k, mid)) {
                max = mid;
            } else {
                min = mid + 1;
            }
        }
        return min;
    }

    private boolean isDayOk(int[] bloomDay, int m, int k, int day) {
        int flower = 0;
        int flowers = 0;
        for (int i = 0; i < bloomDay.length && flowers < m; i++) {
            if (bloomDay[i] <= day) {
                flower++;
                if (flower == k) {
                    flowers++;
                    flower = 0;
                }
            } else {
                flower = 0;
            }
        }
        return flowers >= m;
    }

    public int removeDuplicates(int[] nums) {
        int length = nums.length;
        if (length <= 1) {
            return length;
        }
        int slow = 1;
        int fast = 1;
        while (fast < length) {
            if (nums[fast] != nums[fast - 1]) {
                nums[slow++] = nums[fast];
            }
            fast++;
        }
        return slow;
    }

    public int[] findBall(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            int y = i;
            for (int[] ints : grid) {
                int loc = ints[y];
                if (loc == 1) {
                    if (y + 1 >= n || ints[y + 1] != 1) {
                        res[i] = -1;
                        break;
                    } else {
                        y++;
                    }
                } else {
                    if (y - 1 < 0 || ints[y - 1] != -1) {
                        res[i] = -1;
                        break;
                    } else {
                        y--;
                    }
                }
                res[i] = y;
            }
        }
        return res;
    }

    public int eatenApples(int[] apples, int[] days) {
        int res = 0;
        int length = apples.length;
        Map<Integer, Integer> map = new TreeMap<>();  // 时间,苹果数目
        for (int i = 0; i < length; i++) {
            int count = days[i] + i;
            map.put(count, map.getOrDefault(count, 0) + apples[i]);
            Set<Integer> tmp = new HashSet<>();
            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                if (entry.getKey() <= i || entry.getValue() == 0) {
                    tmp.add(entry.getKey());
                } else {
                    map.put(entry.getKey(), entry.getValue() - 1);
                    res++;
                    break;
                }
            }
            for (int integer : tmp) {
                map.remove(integer);
            }
        }
        int i = length;
        while (!map.isEmpty()) {
            Set<Integer> tmp = new HashSet<>();
            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                if (entry.getKey() <= i || entry.getValue() == 0) {
                    tmp.add(entry.getKey());
                } else {
                    map.put(entry.getKey(), entry.getValue() - 1);
                    res++;
                    break;
                }
            }
            for (int integer : tmp) {
                map.remove(integer);
            }
            i++;
        }
        return res;
    }

    public boolean halvesAreAlike(String s) {
        Set<Character> set = new HashSet<>(16);
        Collections.addAll(set, 'a', 'e','i','o','u','A','E','I','O','U');
        int count0 = 0;
        int count1 = 0;
        int length = s.length();
        for (int i = 0; i < length / 2; i++) {
            if (set.contains(s.charAt(i))) {
                count0++;
            }
            if (set.contains(s.charAt(length - 1 - i))) {
                count1++;
            }
        }
        return count0 == count1;
    }

    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < numRows; i++) {
            List<Integer> list = new ArrayList<>();
            for (int j = 0; j <= i; j++) {
                if (j == 0 || j == i) {
                    list.add(1);
                    continue;
                }
                list.add(res.get(i - 1).get(j - 1) + res.get(i - 1).get(j));
            }
            res.add(list);
        }
        return res;
    }

    public String removeKdigits(String num, int k) {
        if (k == num.length()) {
            return "0";
        }
        StringBuilder res = new StringBuilder(num);
        for (int i = 0; i < k; i++) {
            int index = res.length() - 1;
            for (int j = 0; j < res.length() - 1; j++) {
                // 找第一个值下降的位置
                if (res.charAt(j) > res.charAt(j + 1)) {
                    index = j;
                    break;
                }
            }
            res.deleteCharAt(index);
            while (res.length() > 1 && res.charAt(0) == '0') {
                res.deleteCharAt(0);
            }
        }
        return res.toString();
    }

    public int longestMountain(int[] A) {
        int start = 0;
        int res = 0;
        int end = 0;
        while (start + 2 < A.length) {
            end = start + 1;
            if (A[end] > A[end - 1]) {  // 先上升
                while (end + 1 < A.length && A[end + 1] > A[end]) {  // 找最高点
                    end++;
                }
                if (end + 1 < A.length && A[end +1] < A[end]) {  // 如果下降
                    while (end + 1 < A.length && A[end +1] < A[end]) {
                        end++;  // 找到最低点
                    }
                    res = Math.max(res, end - start +1);
                } else {
                    end++;
                }
            }
            start = end;
        }
        return res;
    }

    public boolean canPartition(int[] nums) {
        int length = nums.length;
        if (length < 2) {
            return false;
        }
        int sum = 0;
        int maxNum = 0;
        for (int num : nums) {
            sum += num;
            maxNum = Math.max(maxNum, num);
        }
        if (sum % 2 == 1) {
            return false;
        }
        int target = sum / 2;
        if (maxNum > target) {
            return false;
        }
        boolean[] flags = new boolean[target + 1];  // 能否组成i
        flags[0] = true;
        for (int num : nums) {
            for (int i = target; i >= num; i--) {
                flags[i] |= flags[i - num];
            }
            if (flags[target]) {
                return true;
            }
        }
        return flags[target];
    }

    public void flatten(TreeNode root) {
        if (root == null) {
            return;
        }
        // 先把左右摊平，右接左后，左转右
        flatten(root.left);
        flatten(root.right);
        if (root.left != null) {
            TreeNode left = root.left;
            TreeNode right = root.right;
            while (left.right != null) {
                left = left.right;
            }
            left.right = root.right;
            root.right = root.left;
            root.left = null;
        }
    }

    public int firstMissingPositive(int[] nums) {
        int length = nums.length;
        int index = 1;
        while (index < length) {
            if (nums[index] > 0 && nums[index] <= length && nums[index] != nums[nums[index] - 1]) {
                swap(nums, index, nums[index] - 1);
            } else {
                index++;
            }
        }
        int res = length + 1;
        for (int i = 0; i < length; i++) {
            if (nums[i] != i + 1) {
                res = i + 1;
                break;
            }
        }
        return res;
    }

    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }

    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) {
            return root;
        }
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        if (left != null && right != null) {
            return root;
        } else if (left != null) {
            return left;
        } else if (right != null) {
            return right;
        } else {
            return null;
        }
    }

    public int[] sumZero(int n) {
        int[] res = new int[n];
        int index = 0;
        if ((n & 1) == 1) {
            index++;
        }
        int num = 1;
        for (int i = 0; i < n / 2; i++) {
            res[index++] = num;
            res[index++] = -1 * num;
            num++;
        }
        return res;
    }

    public String reformat(String s) {
        StringBuilder chars = new StringBuilder();
        StringBuilder nums = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (c >= '0' && c <= '9') {
                nums.append(c);
            } else {
                chars.append(c);
            }
        }
        if (Math.abs(chars.length() - nums.length()) > 1) {
            return "";
        }
        StringBuilder res = new StringBuilder();
        int a = 0;
        int b = 0;
        while (a < chars.length() && b < nums.length()) {
            res.append(chars.charAt(a)).append(nums.charAt(b));
            a++;
            b++;
        }
        if (a < chars.length()) {
            res.append(chars.charAt(a));
        }
        if (b < nums.length()) {
            res.insert(0, nums.charAt(b));
        }
        return res.toString();
    }

    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        boolean[] flags = new boolean[nums.length];
        getPermute(res, nums, flags, 0, new ArrayList<>());
        return res;
    }

    private void getPermute(List<List<Integer>> res, int[] nums, boolean[] flags, int count, List<Integer> permute) {
        if (count == nums.length) {
            List<Integer> list = new ArrayList<>(permute);
            res.add(list);
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            if (!flags[i]) {
                permute.add(num);
                flags[i] = true;
                getPermute(res, nums, flags, count + 1, permute);
                flags[i] = false;
                permute.remove(permute.size() - 1);
            }
        }
    }

    public int[] processQueries(int[] queries, int m) {
        int length = queries.length;
        int[] res = new int[length];
        List<Integer> list = new LinkedList<>();
        for (int i = 1; i <= m; i++) {
            list.add(i);
        }
        for (int i = 0; i < length; i++) {
            res[i] = list.indexOf(queries[i]);
            list.remove(new Integer(queries[i]));
            list.add(0, queries[i]);
        }
        return res;
    }

    public List<String> stringMatching(String[] words) {
        List<String> res = new ArrayList<>(words.length);
        for (int i = 0; i < words.length; i++) {
            for (int j = 0; j < words.length; j++) {
                if (j != i) {
                    if (words[j].contains(words[i])) {
                        res.add(words[i]);
                        break;
                    }
                }
            }
        }
        return res;
    }

    public double[] intersection(int[] start1, int[] end1, int[] start2, int[] end2) {
        int x1 = start1[0], y1 = start1[1];
        int x2 = end1[0], y2 = end1[1];
        int x3 = start2[0], y3 = start2[1];
        int x4 = end2[0], y4 = end2[1];

        double[] ans = new double[2];
        Arrays.fill(ans, Double.MAX_VALUE);
        // 判断两直线是否平行
        if ((y4-y3)*(x2-x1) == (y2-y1)*(x4-x3)) {
            // 判断两直线是否重叠
            if ((y2-y1)*(x3-x1) == (y3-y1)*(x2-x1)) {
                // 判断 (x3, y3) 是否在「线段」(x1, y1)~(x2, y2) 上
                if (isInside(x1, y1, x2, y2, x3, y3)) {
                    updateRes(ans, x3, y3);
                }
                // 判断 (x4, y4) 是否在「线段」(x1, y1)~(x2, y2) 上
                if (isInside(x1, y1, x2, y2, x4, y4)) {
                    updateRes(ans, (double)x4, (double)y4);
                }
                // 判断 (x1, y1) 是否在「线段」(x3, y3)~(x4, y4) 上
                if (isInside(x3, y3, x4, y4, x1, y1)) {
                    updateRes(ans, (double)x1, (double)y1);
                }
                // 判断 (x2, y2) 是否在「线段」(x3, y3)~(x4, y4) 上
                if (isInside(x3, y3, x4, y4, x2, y2)) {
                    updateRes(ans, (double)x2, (double)y2);
                }
            }
        } else {
            // 联立方程得到 t1 和 t2 的值
            double t1 = (double)(x3 * (y4 - y3) + y1 * (x4 - x3) - y3 * (x4 - x3) - x1 * (y4 - y3)) / ((x2 - x1) * (y4 - y3) - (x4 - x3) * (y2 - y1));
            double t2 = (double)(x1 * (y2 - y1) + y3 * (x2 - x1) - y1 * (x2 - x1) - x3 * (y2 - y1)) / ((x4 - x3) * (y2 - y1) - (x2 - x1) * (y4 - y3));
            // 判断 t1 和 t2 是否均在 [0, 1] 之间
            if (t1 >= 0.0 && t1 <= 1.0 && t2 >= 0.0 && t2 <= 1.0) {
                ans[0] = x1 + t1 * (x2 - x1);
                ans[1] = y1 + t1 * (y2 - y1);
            }
        }
        if (ans[0] == Double.MAX_VALUE) {
            return new double[0];
        }
        return ans;
    }

    // 判断 (x, y) 是否在「线段」(x1, y1)~(x2, y2) 上
    // 这里的前提是 (x, y) 一定在「直线」(x1, y1)~(x2, y2) 上
    private boolean isInside(int x1, int y1, int x2, int y2, int x, int y) {
        // 若与 x 轴平行，只需要判断 x 的部分
        // 若与 y 轴平行，只需要判断 y 的部分
        // 若为普通线段，则都要判断
        return (x1 == x2 || (Math.min(x1, x2) <= x && x <= Math.max(x1, x2)))
                && (y1 == y2 || (Math.min(y1, y2) <= y && y <= Math.max(y1, y2)));
    }

    private void updateRes(double[] ans, double x, double y) {
        if (x < ans[0] || (x == ans[0] && y < ans[1])) {
            ans[0] = x;
            ans[1] = y;
        }
    }

    public int maxDistance(int[][] grid) {
        int[] dx = {0, 0, 1, -1};
        int[] dy = {1, -1, 0, 0};

        Queue<int[]> queue = new ArrayDeque<>();
        int m = grid.length, n = grid[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 1) {
                    queue.offer(new int[] {i, j});
                }
            }
        }

        boolean hasOcean = false;
        int[] point = null;
        while (!queue.isEmpty()) {
            point = queue.poll();
            int x = point[0], y = point[1];
            for (int i = 0; i < 4; i++) {
                int newX = x + dx[i];
                int newY = y + dy[i];
                if (newX < 0 || newX >= m || newY < 0 || newY >= n || grid[newX][newY] != 0) {
                    continue;
                }
                grid[newX][newY] = grid[x][y] + 1;
                hasOcean = true;
                queue.offer(new int[] {newX, newY});
            }
        }

        if (point == null || !hasOcean) {
            return -1;
        }

        return grid[point[0]][point[1]] - 1;

    }
}

class TreeNode {
     int val;
     TreeNode left;
     TreeNode right;
     TreeNode(int x) { val = x; }
}