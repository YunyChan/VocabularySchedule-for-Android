package com.example.vocabularyschedule;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter{
	private Context context;
	private List<TableRow> table; 
	private View[] viewArray;
	
	public ListViewAdapter(Context context, List<TableRow> table) {  
        this.context = context;  
        this.table = table; 
        viewArray = new View[table.size()];
    }  

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return table.size(); 
	}

	@Override
	public TableRow getItem(int arg0) {
		// TODO Auto-generated method stub
		return table.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (viewArray[position]==null){
        	TableRow tableRow = table.get(position); 
        	viewArray[position] = new TableRowView(this.context, tableRow); 
        }
        return viewArray[position];
	}
	
    /** 
     * TableRowView 实现表格行的样式 
     * @author hellogv 
     */  
    class TableRowView extends LinearLayout {  
        public TableRowView(Context context, TableRow tableRow) {  
            super(context);  
              
            this.setOrientation(LinearLayout.HORIZONTAL);  
            for (int i = 0; i < tableRow.getSize(); i++) {//逐个格单元添加到行  
                TableCell tableCell = tableRow.getCellValue(i);  
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(  
                        tableCell.width, tableCell.height);//按照格单元指定的大小设置空间  
                layoutParams.setMargins(0, 0, 1, 1);//预留空隙制造边框  
                if (tableCell.type == TableCell.STRING) {//如果格单元是文本内容  
                    TextView textCell = new TextView(context);  
                    textCell.setLines(1);  
                    textCell.setGravity(Gravity.CENTER);  
                    textCell.setBackgroundColor(Color.WHITE);//背景白
                    textCell.setText(String.valueOf(tableCell.value));  
                    addView(textCell, layoutParams);  
                } else if (tableCell.type == TableCell.IMAGE) {//如果格单元是图像内容  
                    ImageView imgCell = new ImageView(context);  
                    imgCell.setBackgroundColor(Color.WHITE);//背景黑色  
                    imgCell.setImageResource((Integer) tableCell.value);  
                    addView(imgCell, layoutParams);  
                }  else if (tableCell.type == TableCell.CHECKBOX) {
                	CheckBox checkBoxCell=new CheckBox(context);
                	checkBoxCell.setBackgroundColor(Color.WHITE);
                	checkBoxCell.setChecked((Boolean)tableCell.value);
                	addView(checkBoxCell, layoutParams);
				}else if (tableCell.type == TableCell.ITEM) {
					LinearLayout item=new LinearLayout(context);
					item.setOrientation(LinearLayout.VERTICAL);					
					LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(  
	                        tableCell.width, 25);

					TextView textCell = new TextView(context);
					textCell.setLines(1);
					textCell.setGravity(Gravity.LEFT);
					textCell.setBackgroundColor(Color.WHITE);//背景白
					textCell.setText(String.valueOf(1));
					item.addView(textCell, itemParams);
					
					itemParams = new LinearLayout.LayoutParams(  
	                        tableCell.width, 20);
					textCell = new TextView(context);
					textCell.setLines(1);
					textCell.setGravity(Gravity.CENTER);
					textCell.setBackgroundColor(Color.WHITE);//背景白
					textCell.setText("~");
					item.addView(textCell, itemParams);
					
					itemParams = new LinearLayout.LayoutParams(  
	                        tableCell.width, 25);
					textCell = new TextView(context);
					textCell.setLines(1);
					textCell.setGravity(Gravity.RIGHT);
					textCell.setBackgroundColor(Color.WHITE);//背景白
					textCell.setText(String.valueOf(30));
					item.addView(textCell, itemParams);
					
					
					this.addView(item,layoutParams);
				}
            }  
            this.setBackgroundColor(Color.BLACK);//背景白色，利用空隙来实现边框              
        }  
    }  
    /** 
     * TableRow 实现表格的行 
     * @author hellogv 
     */  
    static public class TableRow {  
        private TableCell[] cell;  
        public TableRow(TableCell[] cell) {  
            this.cell = cell;  
        }  
        public int getSize() {  
            return cell.length;  
        }  
        public TableCell getCellValue(int index) {  
            if (index >= cell.length)  
                return null;  
            return cell[index];  
        }  
    }  
    /** 
     * TableCell 实现表格的格单元 
     * @author hellogv 
     */  
    static public class TableCell {  
        static public final int STRING = 0;  
        static public final int IMAGE = 1;  
        static public final int CHECKBOX = 2; 
        static public final int ITEM = 3; 
        public Object value;  
        public int width;  
        public int height;  
        private int type;  
        public TableCell(Object value, int width, int height, int type) {  
            this.value = value;  
            this.width = width;  
            this.height = height;  
            this.type = type;  
        }  
    }  

}

