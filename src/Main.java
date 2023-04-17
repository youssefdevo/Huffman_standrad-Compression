import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
class Node {
    int frq;
    char c;
    Node left;
    Node right;
}

public class Main {

    public static HashMap<Character, String> ans = new HashMap<>();

    public static void compress(Node root, String code) {
        if (root.left == null && root.right == null) {

            //save the table.
            ans.put(root.c, code);

            return;
        }
        compress(root.left, code + "0");
        compress(root.right, code + "1");
    }


    public static String deCompress(String key, HashMap<String, String> hashMap) {
        String ans = "", temp = "";
        for (int i = 0; i < key.length(); i++) {
            temp += key.charAt(i);
            if (hashMap.containsKey(temp)) {
                ans += hashMap.get(temp);
                temp = "";
            }
        }
        return ans;
    }

    public static void main(String[] args) {

        System.out.println("1-Compression.\n2-Decompression.");
        Scanner in = new Scanner(System.in);
        int ord = in.nextInt();
        if (ord == 1) {

            //read from file.
            String input = "";

            try {
                File Obj = new File("data.txt");
                Scanner Reader = new Scanner(Obj);
                input = Reader.nextLine();
                Reader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error has occurred.");
                e.printStackTrace();
            }


            HashMap<Character, Integer> mp = new HashMap<>();
            for (int i = 0; i < input.length(); i++) {

                if (mp.containsKey(input.charAt(i))) {
                    int x = mp.get(input.charAt(i));
                    mp.put(input.charAt(i), x + 1);
                } else {
                    mp.put(input.charAt(i), 1);
                }
            }
            PriorityQueue<Node> queue = new PriorityQueue<>(new Comparator<Node>() {
                @Override
                public int compare(Node o1, Node o2) {
                    if (o1.frq > o2.frq) return 1;
                    if (o1.frq < o2.frq) return -1;
                    return 0;
                }
            });

            for (Map.Entry<Character, Integer> entry : mp.entrySet()) {

                Node newNode = new Node();
                newNode.c = entry.getKey();
                newNode.frq = entry.getValue();
                newNode.left = null;
                newNode.right = null;
                queue.add(newNode);
            }

            Node root = null;

            while (queue.size() > 1) {

                Node n1 = queue.peek();
                queue.poll();

                Node n2 = queue.peek();
                queue.poll();

                Node newRoot = new Node();

                newRoot.frq = n1.frq + n2.frq;
                newRoot.c = ' ';
                newRoot.left = n1;
                newRoot.right = n2;
                root = newRoot;
                queue.add(newRoot);
            }
            compress(root, "");

            // Original size && compressed size.
            int Original = input.length() * 8;
            int compressedSize = 0;
            for (Map.Entry<Character, Integer> entry : mp.entrySet()) {
                int frq = entry.getValue();
                int numOfBits = ans.get(entry.getKey()).length();
                compressedSize += frq * numOfBits;
            }
            try {
                FileWriter Writer = new FileWriter("OutputAfterCompress.txt");

                for (Map.Entry<Character, String> entry : ans.entrySet()) {
                    char c = entry.getKey();
                    String value = entry.getValue();
                    Writer.write("The short Code of character " + c + " is : " + value + "\n");
                }


                Writer.write("\nThe original size = " + Original + "\n");
                Writer.write("The compressed size = " + compressedSize + "\n");
                Writer.close();
            } catch (IOException e) {
                System.out.println("An error has occurred.");
                e.printStackTrace();
            }

        } else {

            String code = "";
            HashMap<String, String> mp = new HashMap<>();
            try {
                File Obj = new File("Decompress.txt");
                Scanner Reader = new Scanner(Obj);
                code = Reader.nextLine();
                String size = Reader.nextLine();
                int n = Integer.valueOf(size);
                for (int i = 0; i < n; i++) {
                    String c = Reader.nextLine();
                    String value = Reader.nextLine();
                    mp.put(value, c);
                }
                Reader.close();
            } catch (FileNotFoundException e) {
                System.out.println("An error has occurred.");
                e.printStackTrace();
            }

            String ans = deCompress(code, mp);
            try {
                FileWriter Writer = new FileWriter("OutputAfterDecompress.txt");
                Writer.write("The message after Decompressed = " + ans);
                Writer.close();
            } catch (IOException e) {
                System.out.println("An error has occurred.");
                e.printStackTrace();
            }
        }
    }
}