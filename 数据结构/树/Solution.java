import java.util.ArrayList;
import java.util.List;

class Solution {
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> list = new ArrayList<>();
        mid(root, list);
        return list;
    }

    void mid(TreeNode node, List<Integer> list) {
        if (node == null) {
            return;
        }
        mid(node.left, list);
        list.add(node.val);
        mid(node.right, list);
    }
}