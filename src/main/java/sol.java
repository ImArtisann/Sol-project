import com.paymennt.crypto.bip32.Network;
import com.paymennt.crypto.bip32.wallet.AbstractWallet;
import com.paymennt.solanaj.api.rpc.Cluster;
import com.paymennt.solanaj.api.rpc.SolanaRpcClient;
import com.paymennt.solanaj.data.SolanaAccount;
import com.paymennt.solanaj.data.SolanaPublicKey;
import com.paymennt.solanaj.data.SolanaTransaction;
import com.paymennt.solanaj.program.SystemProgram;
import com.paymennt.solanaj.wallet.SolanaWallet;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class sol {

    public mongo mongo = new mongo();


    public void run(String receiverAddress, Double withdraw){
        // the netowrk, MAINNET or TESTNET
        final Network network = Network.TESTNET;

        // your mnemonic phrase
        final String mnemonic = "";

        // optional passphrase
        final String passphrase = null;

        // create wallet
        SolanaWallet solanaWallet = new SolanaWallet(mnemonic, passphrase, network);

        // get address (account, chain, index), used to receive
        solanaWallet.getAddress(0, AbstractWallet.Chain.EXTERNAL, null);

        // get private key (account, chain, index), used to sign transactions
        solanaWallet.getPrivateKey(0, AbstractWallet.Chain.EXTERNAL, null);

        // create new SolanaRpcClient, (DEVNET, TESTNET, MAINNET)
        SolanaRpcClient client = new SolanaRpcClient(Cluster.DEVNET);
        // wallet address of the receiver
//        String receiverAddress = "C6wQNnTmbREk4twdQ9T5AXD1A9RxkLZNcMeGT1zgXYMd";

        // amount to transfer in lamports, 1 SOL = 1000000000 lamports
        Double amount = 1000000000.00 * withdraw;

        // create new transaction
        SolanaTransaction transaction = new SolanaTransaction();

        // create solana account, this account holds the funds that we want to transfer
        SolanaAccount account = new SolanaAccount(solanaWallet.getPrivateKey(0, AbstractWallet.Chain.EXTERNAL, null));

        // define the sender and receiver public keys
        SolanaPublicKey fromPublicKey = account.getPublicKey();
        SolanaPublicKey toPublickKey = new SolanaPublicKey(receiverAddress);

        // add instructions to the transaction (from, to, lamports)
        Double newData = new Double(amount);

        // convert into int
        long value = newData.longValue();
        System.out.println(value);
        transaction.addInstruction(SystemProgram.transfer(fromPublicKey, toPublickKey,value));

        // set the recent blockhash
        transaction.setRecentBlockHash(client.getApi().getRecentBlockhash());

        // set the fee payer
        transaction.setFeePayer(account.getPublicKey());

        // sign the transaction
        transaction.sign(account);

        // publish the transaction
        String signature = client.getApi().sendTransaction(transaction);
    }

    public boolean greaterThan20(){

        Document query = new Document("balance", new Document("$gt", 0));
        List<Document> results = new ArrayList<>();
        mongo.mongoCo.find(query).into(results);
        Double playerBal = 0.000;
        SolanaRpcClient client = new SolanaRpcClient(Cluster.DEVNET);

        for(Document doc : results){
            playerBal += doc.getDouble("balance");
        }
        long houseLong = client.getApi().getBalance("");
        Double houseBal = (double)houseLong/1000000000.00;
        Double percent =  houseBal - (80 * houseBal) / 100.0;
        Double check = houseBal-playerBal;


        if(percent<check){
            return true;
        }else{
            System.out.println("House wallet would have less than 20% user is auto losing: " + percent + " " + check);
            return false;
        }

    }

}
