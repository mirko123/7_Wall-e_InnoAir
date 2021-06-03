package com.example.mainapp.write;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.nfc.tech.MifareClassic;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.TextViewCompat;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.mainapp.write.Common;
import com.example.mainapp.write.MCReader;
import com.example.mainapp.R;

public class MyTagWriter {
    public MyTagWriter() {

    }
    private HashMap<Integer, HashMap<Integer, byte[]>> mDumpWithPos;

    public void initDumpWithPosFromDump(String[] dump) {
        mDumpWithPos = new HashMap<>();
        int sector = 0;
        int block = 0;
        // Transform the simple dump array into a structure (mDumpWithPos)
        // where the sector and block information are known additionally.
        // Blocks containing unknown data ("-") are dropped.
        for (int i = 0; i < dump.length; i++) {
            if (dump[i].startsWith("+")) {
                String[] tmp = dump[i].split(": ");
                sector = Integer.parseInt(tmp[tmp.length-1]);
                block = 0;
                mDumpWithPos.put(sector, new HashMap<>());
            } else if (!dump[i].contains("-")) {
                // Use static Access Conditions for all sectors?
                mDumpWithPos.get(sector).put(block++,
                        Common.hex2Bytes(dump[i]));
            } else {
                block++;
            }
        }
        int a = 2;
    }
    private boolean isSectorInRage(boolean isWriteBlock) {
//        MCReader reader = Common.checkForTagAndCreateReader(this);
        MCReader reader = Common.checkForTagAndCreateReader(Common.mAppContext);
        if (reader == null) {
            return false;
        }
        int lastValidSector = reader.getSectorCount() - 1;
        int lastSector;
        reader.close();
        // Initialize last sector.
//        if (isWriteBlock) {
//            lastSector = Integer.parseInt(
//                    mSectorTextBlock.getText().toString());
//        } else {
//            lastSector = Collections.max(mDumpWithPos.keySet());
//        }
        lastSector = Collections.max(mDumpWithPos.keySet());

        // Is last sector in range?
        if (lastSector > lastValidSector) {
            // Error. Tag too small for dump.
            Toast.makeText(Common.mAppContext, R.string.info_tag_too_small,
                    Toast.LENGTH_LONG).show();
            reader.close();
            return false;
        }
        return true;
    }
    public void checkDumpAgainstTag() {
        // Create reader.
        MCReader reader = Common.checkForTagAndCreateReader(Common.mAppContext);
        if (reader == null) {
            Toast.makeText(Common.mAppContext, R.string.info_tag_lost_check_dump,
                    Toast.LENGTH_LONG).show();
            return;
        }

        // Check if tag is correct size for dump.
        if (reader.getSectorCount()-1 < Collections.max(
                mDumpWithPos.keySet())) {
            // Error. Tag too small for dump.
            Toast.makeText(Common.mAppContext, R.string.info_tag_too_small,
                    Toast.LENGTH_LONG).show();
            reader.close();
            return;
        }

        // Check if tag is writable on needed blocks.
        // Reformat for reader.isWritableOnPosition(...).
        final SparseArray<byte[][]> keyMap  =
                Common.getKeyMap();
        HashMap<Integer, int[]> dataPos =
                new HashMap<>(mDumpWithPos.size());
        for (int sector : mDumpWithPos.keySet()) {
            int i = 0;
            int[] blocks = new int[mDumpWithPos.get(sector).size()];
            for (int block : mDumpWithPos.get(sector).keySet()) {
                blocks[i++] = block;
            }
            dataPos.put(sector, blocks);
        }
        HashMap<Integer, HashMap<Integer, Integer>> writeOnPos =
                reader.isWritableOnPositions(dataPos, keyMap);
        reader.close();

        if (writeOnPos == null) {
            // Error while checking for keys with write privileges.
            Toast.makeText(Common.mAppContext, R.string.info_tag_lost_check_dump,
                    Toast.LENGTH_LONG).show();
            return;
        }

        // Skip dialog:
        // Build a dialog showing all sectors and blocks containing data
        // that can not be overwritten with the reason why they are not
        // writable. The user can chose to skip all these blocks/sectors
        // or to cancel the whole write procedure.
        List<HashMap<String, String>> list = new
                ArrayList<>();
        final HashMap<Integer, HashMap<Integer, Integer>> writeOnPosSafe =
                new HashMap<>(
                        mDumpWithPos.size());

        // Check for keys that are missing completely (mDumpWithPos vs. keyMap).
        HashSet<Integer> sectors = new HashSet<>();
        for (int sector : mDumpWithPos.keySet()) {
            if (keyMap.indexOfKey(sector) < 0) {
                // Problem. Keys for sector not found.
                addToList(list, Common.mAppContext.getString(R.string.text_sector) + ": " + sector,
                        Common.mAppContext.getString(R.string.text_keys_not_known));
            } else {
                sectors.add(sector);
            }
        }

        // Check for keys with write privileges that are missing (writeOnPos vs. keyMap).
        // Check for blocks (block-parts) that are read-only.
        // Check for issues of block 0 of the dump about to be written.
        // Check the Access Conditions of the dump about to be written.
        for (int sector : sectors) {
            if (writeOnPos.get(sector) == null) {
                // Error. Sector is dead (IO Error) or ACs are invalid.
                addToList(list, Common.mAppContext.getString(R.string.text_sector) + ": " + sector,
                        Common.mAppContext.getString(R.string.text_invalid_ac_or_sector_dead));
                continue;
            }
            byte[][] keys = keyMap.get(sector);
            Set<Integer> blocks = mDumpWithPos.get(sector).keySet();
            for (int block : blocks) {
                boolean isSafeForWriting = true;
                String position = Common.mAppContext.getString(R.string.text_sector) + ": "
                        + sector + ", " + Common.mAppContext.getString(R.string.text_block)
                        + ": " + block;

                // Special block 0 checks.
                if (!false
                        && sector == 0 && block == 0) {
                    // Block 0 is read-only. This is normal. Skip.
                    continue;
                } else if (false
                        && sector == 0 && block == 0) {
                    // Block 0 should be written. Check it.
                    String block0 = Common.bytes2Hex(mDumpWithPos.get(0).get(0));
                    int block0Check = checkBlock0(block0, false);
                    switch (block0Check) {
                        case 1:
                            Toast.makeText(Common.mAppContext, R.string.info_tag_lost_check_dump,
                                    Toast.LENGTH_LONG).show();
                            return;
                        case 2:
                            // BCC not valid. Abort.
                            Toast.makeText(Common.mAppContext, R.string.info_bcc_not_valid,
                                    Toast.LENGTH_LONG).show();
                            return;
                        case 3:
                            addToList(list, position, Common.mAppContext.getString(
                                    R.string.text_block0_warning));
                            break;
                    }
                }

                // Special Access Conditions checks.
                if ((sector < 31 && block == 3) || sector >= 31 && block == 15) {
                    String sectorTrailer = Common.bytes2Hex(
                            mDumpWithPos.get(sector).get(block));
                    int acCheck = checkAccessConditions(sectorTrailer, false);
                    switch (acCheck) {
                        case 1:
                            // Access Conditions not valid. Abort.
                            Toast.makeText(Common.mAppContext, R.string.info_ac_format_error,
                                    Toast.LENGTH_LONG).show();
                            return;
                        case 2:
                            addToList(list, position, Common.mAppContext.getString(
                                    R.string.info_irreversible_acs));
                            break;
                    }
                }

                // Normal write privileges checks.
                int writeInfo = writeOnPos.get(sector).get(block);
                switch (writeInfo) {
                    case 0:
                        // Problem. Block is read-only.
                        addToList(list, position, Common.mAppContext.getString(
                                R.string.text_block_read_only));
                        isSafeForWriting = false;
                        break;
                    case 1:
                        if (keys[0] == null) {
                            // Problem. Key with write privileges (A) not known.
                            addToList(list, position, Common.mAppContext.getString(
                                    R.string.text_write_key_a_not_known));
                            isSafeForWriting = false;
                        }
                        break;
                    case 2:
                        if (keys[1] == null) {
                            // Problem. Key with write privileges (B) not known.
                            addToList(list, position, Common.mAppContext.getString(
                                    R.string.text_write_key_b_not_known));
                            isSafeForWriting = false;
                        }
                        break;
                    case 3:
                        // No Problem. Both keys have write privileges.
                        // Set to key A or B depending on which one is available.
                        writeInfo = (keys[0] != null) ? 1 : 2;
                        break;
                    case 4:
                        if (keys[0] == null) {
                            // Problem. Key with write privileges (A) not known.
                            addToList(list, position, Common.mAppContext.getString(
                                    R.string.text_write_key_a_not_known));
                            isSafeForWriting = false;
                        } else {
                            // Problem. ACs are read-only.
                            addToList(list, position, Common.mAppContext.getString(
                                    R.string.text_ac_read_only));
                        }
                        break;
                    case 5:
                        if (keys[1] == null) {
                            // Problem. Key with write privileges (B) not known.
                            addToList(list, position, Common.mAppContext.getString(
                                    R.string.text_write_key_b_not_known));
                            isSafeForWriting = false;
                        } else {
                            // Problem. ACs are read-only.
                            addToList(list, position, Common.mAppContext.getString(
                                    R.string.text_ac_read_only));
                        }
                        break;
                    case 6:
                        if (keys[1] == null) {
                            // Problem. Key with write privileges (B) not known.
                            addToList(list, position, Common.mAppContext.getString(
                                    R.string.text_write_key_b_not_known));
                            isSafeForWriting = false;
                        } else {
                            // Problem. Keys are read-only.
                            addToList(list, position, Common.mAppContext.getString(
                                    R.string.text_keys_read_only));
                        }
                        break;
                    case -1:
                        // Error. Some strange error occurred. Maybe due to some
                        // corrupted ACs...
                        addToList(list, position, Common.mAppContext.getString(
                                R.string.text_strange_error));
                        isSafeForWriting = false;
                }
                // Add if safe for writing.
                if (isSafeForWriting) {
                    if (writeOnPosSafe.get(sector) == null) {
                        // Create sector.
                        HashMap<Integer, Integer> blockInfo =
                                new HashMap<>();
                        blockInfo.put(block, writeInfo);
                        writeOnPosSafe.put(sector, blockInfo);
                    } else {
                        // Add to sector.
                        writeOnPosSafe.get(sector).put(block, writeInfo);
                    }
                }
            }
        }

        // Show skip/cancel dialog (if needed).
        if (list.size() != 0) {
            // If the user skips all sectors/blocks that are not writable,
            // the writeTag() method will be called.
            int pad = Common.dpToPx(5);
            String[] from = new String[] {"position", "reason"};
            int[] to = new int[] {android.R.id.text1, android.R.id.text2};
        } else {
            // Write.
            writeDump(writeOnPosSafe, keyMap);
        }
    }
    private void writeDump(
            final HashMap<Integer, HashMap<Integer, Integer>> writeOnPos,
            final SparseArray<byte[][]> keyMap) {
        // Check for write data.
        if (writeOnPos.size() == 0) {
            // Nothing to write. Exit.
            Toast.makeText(Common.mAppContext, R.string.info_nothing_to_write,
                    Toast.LENGTH_LONG).show();
            return;
        }

        // Create reader.
        final MCReader reader = Common.checkForTagAndCreateReader(Common.mAppContext);
        if (reader == null) {
            return;
        }

        // Display don't remove warning.
//        LinearLayout ll = new LinearLayout(Common.mAppContext);
//        int pad = Common.dpToPx(20);
//        ll.setPadding(pad, pad, pad, pad);
//        ll.setGravity(Gravity.CENTER);
//        ProgressBar progressBar = new ProgressBar(Common.mAppContext);
//        progressBar.setIndeterminate(true);
//        pad = Common.dpToPx(20);
//        progressBar.setPadding(0, 0, pad, 0);
//        tv.setText((R.string.dialog_wait_write_tag));
//        tv.setTextSize(18);
//        ll.addView(progressBar);
//        ll.addView(tv);
//        final AlertDialog warning = new AlertDialog.Builder(this)
//                .setTitle(R.string.dialog_wait_write_tag_title)
//                .setView(ll)
//                .create();
//        warning.show();


        // Start writing in new thread.
//        final Activity a = this;
        final Handler handler = new Handler(Looper.getMainLooper());
        new Thread(() -> {
            // Write dump to tag.
            for (int sector : writeOnPos.keySet()) {
                byte[][] keys = keyMap.get(sector);
                for (int block : writeOnPos.get(sector).keySet()) {
                    // Select key with write privileges.
                    byte[] writeKey = null;
                    boolean useAsKeyB = true;
                    int wi = writeOnPos.get(sector).get(block);
                    if (wi == 1 || wi == 4) {
                        writeKey = keys[0]; // Write with key A.
                        useAsKeyB = false;
                    } else if (wi == 2 || wi == 5 || wi == 6) {
                        writeKey = keys[1]; // Write with key B.
                    }

                    // Write block.
                    int result = reader.writeBlock(sector, block,
                            mDumpWithPos.get(sector).get(block),
                            writeKey, useAsKeyB);

                    if (result != 0) {
                        // Error. Some error while writing.
                        handler.post(() -> Toast.makeText(Common.mAppContext,
                                R.string.info_write_error,
                                Toast.LENGTH_LONG).show());
                        reader.close();
//                        warning.cancel();
                        return;
                    }
                }
            }
            // Finished writing.
            reader.close();
//            warning.cancel();
            handler.post(() -> Toast.makeText(Common.mAppContext, R.string.info_write_successful,
                    Toast.LENGTH_LONG).show());
//            a.finish();
        }).start();
    }
    private void addToList(List<HashMap<String, String>> list,
                           String position, String reason) {
        HashMap<String, String> item = new HashMap<>();
        item.put( "position", position);
        item.put( "reason", reason);
        list.add(item);
    }
    private int checkAccessConditions(String sectorTrailer, boolean showToasts) {
        // Check if Access Conditions are valid.
        byte[] acBytes = Common.hex2Bytes(sectorTrailer.substring(12, 18));
        byte[][] acMatrix = Common.acBytesToACMatrix(acBytes);
        if (acMatrix == null) {
            // Error. Invalid ACs.
            if (showToasts) {
                Toast.makeText(Common.mAppContext, R.string.info_ac_format_error,
                        Toast.LENGTH_LONG).show();
            }
            return 1;
        }
        // Check if Access Conditions are irreversible.
        boolean keyBReadable = Common.isKeyBReadable(
                acMatrix[0][3], acMatrix[1][3], acMatrix[2][3]);
        int writeAC = Common.getOperationRequirements(
                acMatrix[0][3], acMatrix[1][3], acMatrix[2][3],
                Common.Operation.WriteAC, true, keyBReadable);
        if (writeAC == 0) {
            // Warning. Access Conditions can not be changed after writing.
            if (showToasts) {
                Toast.makeText(Common.mAppContext, R.string.info_irreversible_acs,
                        Toast.LENGTH_LONG).show();
            }
            return 2;
        }
        return 0;
    }
    private int checkBlock0(String block0, boolean showToasts) {
        MCReader reader = Common.checkForTagAndCreateReader(Common.mAppContext);
        if (reader == null) {
            // Error. There is no tag.
            return 1;
        }
        reader.close();
        int uidLen = Common.getUID().length;

        // BCC.
        if (uidLen == 4 ) {
            byte bcc = Common.hex2Bytes(block0.substring(8, 10))[0];
            byte[] uid = Common.hex2Bytes(block0.substring(0, 8));
            boolean isValidBcc = Common.isValidBcc(uid, bcc);
            if (!isValidBcc) {
                // Error. BCC is not valid. Show error message.
                if (showToasts) {
                    Toast.makeText(Common.mAppContext, R.string.info_bcc_not_valid,
                            Toast.LENGTH_LONG).show();
                }
                return 2;
            }
        }

        // SAK & ATQA.
        boolean isValidBlock0 = Common.isValidBlock0(
                block0, uidLen, reader.getSize(), true);
        if (!isValidBlock0) {
            if (showToasts) {
                Toast.makeText(Common.mAppContext, R.string.text_block0_warning,
                        Toast.LENGTH_LONG).show();
            }
            return 3;
        }

        // Everything was O.K.
        return 0;
    }
}
