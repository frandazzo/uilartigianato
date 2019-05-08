package applica.feneal.domain.utils;

public class MergeRemoveDupSort {

    public Integer[] mergeRemoveDupSortIt(Integer[] a, Integer[] b) {
        Integer [] c = mergeIt(a,b);
        Integer [] d = removeIt(c);
        Integer [] e = sortIt(d);
        return e;
    }

    private Integer[] mergeIt(Integer[] a, Integer[] b) {
        Integer[] c = new Integer[a.length + b.length];
        int k=0;
        for (int n : a) c[k++]=n;
        for (int n : b) c[k++]=n;
        return c;
    }

    private Integer[] removeIt(Integer[] c) {
        int len=c.length;
        for (int i=0;i<len-1;i++)
            for (int j=i+1;j<len;j++)
                if (c[i] == c[j]) {
                    for (int k=j;k<len-1;k++)
                        c[k]=c[k+1];
                    --len;
                }
        Integer [] r = new Integer[len];
        for (int i=0;i<r.length;i++)
            r[i]=c[i];
        return r;
    }

    private Integer[] sortIt(Integer[] a) {
        for(int index=0; index<a.length-1; index++)
            for(int i=index+1; i<a.length; i++)
                if(a[index] > a[i]){
                    int temp = a[index];
                    a[index] = a[i];
                    a[i] = temp;
                }
        return a;
    }




}
