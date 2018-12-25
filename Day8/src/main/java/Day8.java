import lombok.Data;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Day8 {

    public static void main(String[] args) throws FileNotFoundException {

        String[] input;

        if (args.length == 0) {
            InputStream inputStream = Day8.class.getResourceAsStream("input_day8.txt");
            input = new BufferedReader(new InputStreamReader(inputStream)).lines().toArray(String[]::new);
        } else {
            input = new BufferedReader(new FileReader(args[0])).lines().toArray(String[]::new);
        }

        long startTime = System.currentTimeMillis();

        NodeData rootNodeData = recursiveSumMetadata(
                Arrays.stream(input[0].split(" ")).mapToInt(Integer::parseInt).toArray()
        );

        System.out.println("--- Day 8: Memory Maneuver ---");

        System.out.println();

        System.out.println(
                "Part One - The sum of all metadata entries :"
        );
        System.out.println(rootNodeData.getSum());

        System.out.println();

        System.out.println(
                "Part Two - The value of the root node :"
        );
        System.out.println(rootNodeData.getRootSum());

        System.out.println();

        long endTime = System.currentTimeMillis();
        System.out.println("Finished in " + (float) (endTime - startTime) / 1000 + " s.");

        System.out.println();
    }

    private static NodeData recursiveSumMetadata(int[] node) {

        int nodeNumber = node[0];
        int metadataNumber = node[1];
        NodeData nodeData = new NodeData();
        nodeData.setRootValues(new ArrayList<>());

        // Stop recursive condition
        if (nodeNumber == 0) {
            nodeData.setSum(Arrays.stream(Arrays.copyOfRange(node, 2, 2 + metadataNumber)).sum());
            nodeData.setNodeTail(Arrays.copyOfRange(node, 2 + metadataNumber, node.length));
            nodeData.setRootSum(nodeData.getSum());
        } else {

            nodeData.setNodeTail(Arrays.copyOfRange(node, 2, node.length));

            // Recursive call for all child node
            for (int i = 0; i < nodeNumber; i++) {
                NodeData nodeDataReturned = recursiveSumMetadata(nodeData.getNodeTail());
                nodeData.setSum(nodeData.getSum() + nodeDataReturned.getSum());
                nodeData.setNodeTail(nodeDataReturned.getNodeTail());
                nodeData.getRootValues().add(nodeDataReturned.getRootSum());
            }

            if (metadataNumber > 0) {

                // Retrieve all metadata values
                int[] metadataValues = Arrays.stream(
                        Arrays.copyOfRange(nodeData.getNodeTail(), 0, metadataNumber)
                ).toArray();

                // Add metadata values to the Sum
                nodeData.setSum(
                        nodeData.getSum() + Arrays.stream(metadataValues).sum()
                );
                // Update NodeTail
                nodeData.setNodeTail(
                        Arrays.copyOfRange(nodeData.getNodeTail(), metadataNumber, nodeData.getNodeTail().length)
                );

                // Calculate the root Sum
                AtomicInteger rootSum = new AtomicInteger();
                Arrays.stream(metadataValues).forEach(i -> {
                    if (i <= nodeData.getRootValues().size()) {
                        rootSum.addAndGet(nodeData.getRootValues().get(i - 1));
                    }
                });
                nodeData.setRootSum(rootSum.intValue());
            }
        }

        return nodeData;
    }

    @Data
    private static class NodeData {
        private int[] nodeTail;
        private int sum;
        private List<Integer> rootValues;
        private int rootSum;
    }

}
