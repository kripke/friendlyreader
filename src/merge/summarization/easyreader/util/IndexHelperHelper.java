/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package merge.summarization.easyreader.util;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author christian
 */
public class IndexHelperHelper implements Comparable{
        private ArrayList<IndexHelper> list;

        public IndexHelperHelper(ArrayList<IndexHelper> l){
            this.list = l;
            Collections.sort(list);
        }

        public int compareTo(Object o) {
            IndexHelperHelper helper = (IndexHelperHelper) o;
            ArrayList<IndexHelper> l = helper.getHelpers();

            if(this.list.isEmpty())
                return -1;
            if(helper.getHelpers().isEmpty())
                return 1;

            if(l.get(0).getValue() > this.list.get(0).getValue())
                return 1;
            else if(l.get(0).equals(this.list.get(0)))
                return 0;
            else
                return -1;

        }

        public ArrayList<IndexHelper> getHelpers(){
            return this.list;
        }

        @Override
        public String toString(){
            return list.toString();
        }

        public boolean remove(int index){
            if(this.list.size() > 0){
                this.list.remove(index);
                return true;
            }
            return false;
        }

    }
