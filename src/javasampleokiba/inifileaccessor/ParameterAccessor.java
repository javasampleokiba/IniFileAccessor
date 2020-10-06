package javasampleokiba.inifileaccessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * INIファイルオブジェクトが持つ値へのアクセスを容易にするクラス
 * 
 * @author javasampleokiba
 */
public class ParameterAccessor {

    private final IniFile ini_;
    private boolean autoTrim_       = true;

    /**
     * 指定されたINIファイルオブジェクトへのアクセスを行う{@code ParameterAccessor}オブジェクトを構築します。
     * 
     * @param ini  INIファイルオブジェクト
     * @throws NullPointerException  {@code ini}が{@code null}の場合
     */
    public ParameterAccessor(IniFile ini) {
        if (ini == null) throw new NullPointerException();
        ini_ = ini;
    }

    /**
     * 値を数値、真偽値として取得する際に、前後の空白文字を自動的にトリムするか判定します。
     * デフォルトは、トリムします。
     * 
     * @return 自動的にトリムする場合は {@code true}
     */
    public boolean isAutoTrim() {
        return autoTrim_;
    }

    /**
     * 値を数値、真偽値として取得する際に、前後の空白文字を自動的にトリムするかを設定します。
     * 
     * @param autoTrim  自動的にトリムするかの真偽値
     */
    public void setAutoTrim(boolean autoTrim) {
        autoTrim_ = autoTrim;
    }

    /**
     * すべてのセクション名を取得します。
     * 
     * @return セクション名のリスト
     */
    public List<String> getSectionNames() {
        List<String> result = new ArrayList<String>();
        for (Section s : ini_.getSections()) {
            result.add(s.getName());
        }
        return result;
    }

    /**
     * 指定されたセクションの持つすべてのパラメータを、値とキーのマップとして取得します。
     * セクションが存在しない場合は{@code null}を返します。
     * パラメータが複数の値を持つ場合は先頭の値のみしか取得できません。
     * 
     * @param section  セクション
     * @return 値とキーのマップ
     */
    public Map<String, String> getParameterMap(String section) {
        Section s = ini_.getSection(section);
        if (s == null) {
            return null;
        }
        Map<String, String> result = new HashMap<String, String>();
        for (Parameter p : s.getParameters()) {
            result.put(p.getKey(), p.getValue());
        }
        return result;
    }

    /**
     * 指定されたセクションの持つすべてのパラメータのキーを取得します。
     * セクションが存在しない場合は{@code null}を返します。
     * 
     * @param section  セクション
     * @return キーのリスト
     */
    public List<String> getKeys(String section) {
        Section s = ini_.getSection(section);
        if (s == null) {
            return null;
        }
        List<String> result = new ArrayList<String>();
        for (Parameter p : s.getParameters()) {
            result.add(p.getKey());
        }
        return result;
    }

    /**
     * 指定されたセクション、キーのパラメータが複数の値を持つか判定します。
     * パラメータが存在しない場合は{@code false}を返します。
     * 
     * @param section  セクション
     * @param key      キー
     * @return 複数の値を持つ場合は {@code true}
     */
    public boolean hasMultiValues(String section, String key) {
        Parameter p = getParameter(section, key);
        if (p == null) {
            return false;
        }
        return p.hasMultiValues();
    }

    /**
     * 指定されたセクション、キーの値を取得します。
     * パラメータが複数の値を持つ場合は先頭の値を取得します。
     * パラメータが存在しない場合は{@code null}を返します。
     * 
     * @param section  セクション
     * @param key      キー
     * @return 値
     */
    public String get(String section, String key) {
        return get(section, key, null);
    }

    /**
     * 指定されたセクション、キーの値を取得します。
     * パラメータが複数の値を持つ場合は先頭の値を取得します。
     * パラメータが存在しない場合は指定されたデフォルト値を返します。
     * 
     * @param section   セクション
     * @param key       キー
     * @param defValue  デフォルト値
     * @return 値
     */
    public String get(String section, String key, String defValue) {
        Parameter p = getParameter(section, key);
        if (p == null) {
            return defValue;
        }
        return p == null ? defValue : p.getValue();
    }

    /**
     * 指定されたセクション、キーのパラメータが持つすべての値を取得します。
     * 
     * @param section  セクション
     * @param key      キー
     * @return 値のリスト
     */
    public List<String> getMulti(String section, String key) {
        Parameter p = getParameter(section, key);
        if (p == null) {
            return new ArrayList<String>();
        }
        return p.getValues();
    }

    /**
     * 指定されたセクション、キーの値をboolean型に変換して取得します。
     * パラメータが複数の値を持つ場合は先頭の値を取得します。
     * パラメータが存在しない場合は{@code false}を返します。
     * 
     * @param section  セクション
     * @param key      キー
     * @return boolean型の値
     */
    public boolean getBoolean(String section, String key) {
        String value = get(section, key);
        if (value != null && autoTrim_) {
            value = value.trim();
        }
        return Boolean.parseBoolean(value);
    }

    /**
     * 指定されたセクション、キーのパラメータが持つすべての値をboolean型に変換して取得します。
     * 
     * @param section  セクション
     * @param key      キー
     * @return 値のリスト
     */
    public List<Boolean> getMultiBoolean(String section, String key) {
        List<Boolean> result = new ArrayList<Boolean>();
        for (String v : getMulti(section, key)) {
            if (v != null && autoTrim_) {
                v = v.trim();
            }
            result.add(Boolean.parseBoolean(v));
        }
        return result;
    }

    /**
     * 指定されたセクション、キーの値をbyte型に変換して取得します。
     * パラメータが複数の値を持つ場合は先頭の値を取得します。
     * パラメータが存在しない、またはbyte型に変換できない場合は0を返します。
     * 
     * @param section  セクション
     * @param key      キー
     * @return byte型の値
     */
    public byte getByte(String section, String key) {
        return getByte(section, key, (byte) 0);
    }

    /**
     * 指定されたセクション、キーの値をbyte型に変換して取得します。
     * パラメータが複数の値を持つ場合は先頭の値を取得します。
     * パラメータが存在しない、またはbyte型に変換できない場合は指定されたデフォルト値を返します。
     * 
     * @param section   セクション
     * @param key       キー
     * @param defValue  デフォルト値
     * @return byte型の値
     */
    public byte getByte(String section, String key, byte defValue) {
        try {
            String value = get(section, key, String.valueOf(defValue));
            if (value != null && autoTrim_) {
                value = value.trim();
            }
            return Byte.parseByte(value);
        } catch (NumberFormatException e) {
            return defValue;
        }
    }

    /**
     * 指定されたセクション、キーのパラメータが持つすべての値をbyte型に変換して取得します。
     * byte型に変換できない場合は指定されたデフォルト値を返します。
     * 
     * @param section   セクション
     * @param key       キー
     * @param defValue  デフォルト値
     * @return 値のリスト
     */
    public List<Byte> getMultiByte(String section, String key, byte defValue) {
        List<Byte> result = new ArrayList<Byte>();
        for (String v : getMulti(section, key)) {
            try {
                if (v != null && autoTrim_) {
                    v = v.trim();
                }
                result.add(Byte.parseByte(v));
            } catch (NumberFormatException e) {
                result.add(defValue);
            }
        }
        return result;
    }

    /**
     * 指定されたセクション、キーの値をshort型に変換して取得します。
     * パラメータが複数の値を持つ場合は先頭の値を取得します。
     * パラメータが存在しない、またはshort型に変換できない場合は0を返します。
     * 
     * @param section  セクション
     * @param key      キー
     * @return short型の値
     */
    public short getShort(String section, String key) {
        return getShort(section, key, (short) 0);
    }

    /**
     * 指定されたセクション、キーの値をshort型に変換して取得します。
     * パラメータが複数の値を持つ場合は先頭の値を取得します。
     * パラメータが存在しない、またはshort型に変換できない場合は指定されたデフォルト値を返します。
     * 
     * @param section   セクション
     * @param key       キー
     * @param defValue  デフォルト値
     * @return short型の値
     */
    public short getShort(String section, String key, short defValue) {
        try {
            String value = get(section, key, String.valueOf(defValue));
            if (value != null && autoTrim_) {
                value = value.trim();
            }
            return Short.parseShort(value);
        } catch (NumberFormatException e) {
            return defValue;
        }
    }

    /**
     * 指定されたセクション、キーのパラメータが持つすべての値をshort型に変換して取得します。
     * short型に変換できない場合は指定されたデフォルト値を返します。
     * 
     * @param section   セクション
     * @param key       キー
     * @param defValue  デフォルト値
     * @return 値のリスト
     */
    public List<Short> getMultiShort(String section, String key, short defValue) {
        List<Short> result = new ArrayList<Short>();
        for (String v : getMulti(section, key)) {
            try {
                if (v != null && autoTrim_) {
                    v = v.trim();
                }
                result.add(Short.parseShort(v));
            } catch (NumberFormatException e) {
                result.add(defValue);
            }
        }
        return result;
    }

    /**
     * 指定されたセクション、キーの値をint型に変換して取得します。
     * パラメータが複数の値を持つ場合は先頭の値を取得します。
     * パラメータが存在しない、またはint型に変換できない場合は0を返します。
     * 
     * @param section  セクション
     * @param key      キー
     * @return int型の値
     */
    public int getInt(String section, String key) {
        return getInt(section, key, 0);
    }

    /**
     * 指定されたセクション、キーの値をint型に変換して取得します。
     * パラメータが複数の値を持つ場合は先頭の値を取得します。
     * パラメータが存在しない、またはint型に変換できない場合は指定されたデフォルト値を返します。
     * 
     * @param section   セクション
     * @param key       キー
     * @param defValue  デフォルト値
     * @return int型の値
     */
    public int getInt(String section, String key, int defValue) {
        try {
            String value = get(section, key, String.valueOf(defValue));
            if (value != null && autoTrim_) {
                value = value.trim();
            }
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defValue;
        }
    }

    /**
     * 指定されたセクション、キーのパラメータが持つすべての値をint型に変換して取得します。
     * int型に変換できない場合は指定されたデフォルト値を返します。
     * 
     * @param section   セクション
     * @param key       キー
     * @param defValue  デフォルト値
     * @return 値のリスト
     */
    public List<Integer> getMultiInt(String section, String key, int defValue) {
        List<Integer> result = new ArrayList<Integer>();
        for (String v : getMulti(section, key)) {
            try {
                if (v != null && autoTrim_) {
                    v = v.trim();
                }
                result.add(Integer.parseInt(v));
            } catch (NumberFormatException e) {
                result.add(defValue);
            }
        }
        return result;
    }

    /**
     * 指定されたセクション、キーの値をlong型に変換して取得します。
     * パラメータが複数の値を持つ場合は先頭の値を取得します。
     * パラメータが存在しない、またはlong型に変換できない場合は0を返します。
     * 
     * @param section  セクション
     * @param key      キー
     * @return long型の値
     */
    public long getLong(String section, String key) {
        return getLong(section, key, 0L);
    }

    /**
     * 指定されたセクション、キーの値をlong型に変換して取得します。
     * パラメータが複数の値を持つ場合は先頭の値を取得します。
     * パラメータが存在しない、またはlong型に変換できない場合は指定されたデフォルト値を返します。
     * 
     * @param section   セクション
     * @param key       キー
     * @param defValue  デフォルト値
     * @return long型の値
     */
    public long getLong(String section, String key, long defValue) {
        try {
            String value = get(section, key, String.valueOf(defValue));
            if (value != null && autoTrim_) {
                value = value.trim();
            }
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return defValue;
        }
    }

    /**
     * 指定されたセクション、キーのパラメータが持つすべての値をlong型に変換して取得します。
     * long型に変換できない場合は指定されたデフォルト値を返します。
     * 
     * @param section   セクション
     * @param key       キー
     * @param defValue  デフォルト値
     * @return 値のリスト
     */
    public List<Long> getMultiLong(String section, String key, long defValue) {
        List<Long> result = new ArrayList<Long>();
        for (String v : getMulti(section, key)) {
            try {
                if (v != null && autoTrim_) {
                    v = v.trim();
                }
                result.add(Long.parseLong(v));
            } catch (NumberFormatException e) {
                result.add(defValue);
            }
        }
        return result;
    }

    /**
     * 指定されたセクション、キーの値をfloat型に変換して取得します。
     * パラメータが複数の値を持つ場合は先頭の値を取得します。
     * パラメータが存在しない、またはfloat型に変換できない場合は0.0を返します。
     * 
     * @param section  セクション
     * @param key      キー
     * @return float型の値
     */
    public float getFloat(String section, String key) {
        return getFloat(section, key, 0.0F);
    }

    /**
     * 指定されたセクション、キーの値をfloat型に変換して取得します。
     * パラメータが複数の値を持つ場合は先頭の値を取得します。
     * パラメータが存在しない、またはfloat型に変換できない場合は指定されたデフォルト値を返します。
     * 
     * @param section   セクション
     * @param key       キー
     * @param defValue  デフォルト値
     * @return float型の値
     */
    public float getFloat(String section, String key, float defValue) {
        try {
            String value = get(section, key, String.valueOf(defValue));
            if (value != null && autoTrim_) {
                value = value.trim();
            }
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return defValue;
        }
    }

    /**
     * 指定されたセクション、キーのパラメータが持つすべての値をfloat型に変換して取得します。
     * float型に変換できない場合は指定されたデフォルト値を返します。
     * 
     * @param section   セクション
     * @param key       キー
     * @param defValue  デフォルト値
     * @return 値のリスト
     */
    public List<Float> getMultiFloat(String section, String key, float defValue) {
        List<Float> result = new ArrayList<Float>();
        for (String v : getMulti(section, key)) {
            try {
                if (v != null && autoTrim_) {
                    v = v.trim();
                }
                result.add(Float.parseFloat(v));
            } catch (NumberFormatException e) {
                result.add(defValue);
            }
        }
        return result;
    }

    /**
     * 指定されたセクション、キーの値をdouble型に変換して取得します。
     * パラメータが複数の値を持つ場合は先頭の値を取得します。
     * パラメータが存在しない、またはdouble型に変換できない場合は0.0を返します。
     * 
     * @param section  セクション
     * @param key      キー
     * @return double型の値
     */
    public double getDouble(String section, String key) {
        return getDouble(section, key, 0.0F);
    }

    /**
     * 指定されたセクション、キーの値をdouble型に変換して取得します。
     * パラメータが複数の値を持つ場合は先頭の値を取得します。
     * パラメータが存在しない、またはdouble型に変換できない場合は指定されたデフォルト値を返します。
     * 
     * @param section   セクション
     * @param key       キー
     * @param defValue  デフォルト値
     * @return double型の値
     */
    public double getDouble(String section, String key, double defValue) {
        try {
            String value = get(section, key, String.valueOf(defValue));
            if (value != null && autoTrim_) {
                value = value.trim();
            }
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defValue;
        }
    }

    /**
     * 指定されたセクション、キーのパラメータが持つすべての値をdouble型に変換して取得します。
     * double型に変換できない場合は指定されたデフォルト値を返します。
     * 
     * @param section   セクション
     * @param key       キー
     * @param defValue  デフォルト値
     * @return 値のリスト
     */
    public List<Double> getMultiDouble(String section, String key, double defValue) {
        List<Double> result = new ArrayList<Double>();
        for (String v : getMulti(section, key)) {
            try {
                if (v != null && autoTrim_) {
                    v = v.trim();
                }
                result.add(Double.parseDouble(v));
            } catch (NumberFormatException e) {
                result.add(defValue);
            }
        }
        return result;
    }

    private Parameter getParameter(String section, String key) {
        Section s = ini_.getSection(section);
        if (s == null) {
            return null;
        }
        return s.getParameter(key);
    }
}