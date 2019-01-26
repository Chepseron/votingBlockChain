package maaifblock;

import java.util.ArrayList;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VotingChain {

    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    public static int difficulty = 0;

    public static void main(String[] args) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            for (int i = 0; i < 10; i++) {
                String data = br.readLine();

                if (blockchain.size() - 1 < 0) {
                    blockchain.add(new Block(data, "0"));
                    System.out.println("Trying to Mine block " + i + "... ");
                    blockchain.get(i).mineBlock(difficulty);
                } else {
                    blockchain.add(new Block(data, blockchain.get(blockchain.size() - 1).hash));
                    System.out.println("Trying to Mine block " + i + "... ");
                    blockchain.get(i).mineBlock(difficulty);
                }
            }
            System.out.println("\nBlockchain is Valid: " + isChainValid());
            String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
            System.out.println("\nThe block chain: ");
            System.out.println(blockchainJson);
        } catch (IOException ex) {
            Logger.getLogger(VotingChain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        for (int i = 1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i - 1);
            if (!currentBlock.hash.equals(currentBlock.calculateHash())) {
                System.out.println("Current Hashes not equal");
                return false;
            }
            if (!previousBlock.hash.equals(currentBlock.previousHash)) {
                System.out.println("Previous Hashes not equal");
                return false;
            }
            if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget)) {
                System.out.println("This block hasn't been mined");
                return false;
            }
        }
        return true;
    }
}
