package javasampleokiba.inifileaccessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * INI�t�@�C���I�u�W�F�N�g�����l�ւ̃A�N�Z�X��e�Ղɂ���N���X
 * 
 * @author javasampleokiba
 */
public class ParameterAccessor {

    private final IniFile ini_;
    private boolean autoTrim_       = true;

    /**
     * �w�肳�ꂽINI�t�@�C���I�u�W�F�N�g�ւ̃A�N�Z�X���s��{@code ParameterAccessor}�I�u�W�F�N�g���\�z���܂��B
     * 
     * @param ini  INI�t�@�C���I�u�W�F�N�g
     * @throws NullPointerException  {@code ini}��{@code null}�̏ꍇ
     */
    public ParameterAccessor(IniFile ini) {
        if (ini == null) throw new NullPointerException();
        ini_ = ini;
    }

    /**
     * �l�𐔒l�A�^�U�l�Ƃ��Ď擾����ۂɁA�O��̋󔒕����������I�Ƀg�������邩���肵�܂��B
     * �f�t�H���g�́A�g�������܂��B
     * 
     * @return �����I�Ƀg��������ꍇ�� {@code true}
     */
    public boolean isAutoTrim() {
        return autoTrim_;
    }

    /**
     * �l�𐔒l�A�^�U�l�Ƃ��Ď擾����ۂɁA�O��̋󔒕����������I�Ƀg�������邩��ݒ肵�܂��B
     * 
     * @param autoTrim  �����I�Ƀg�������邩�̐^�U�l
     */
    public void setAutoTrim(boolean autoTrim) {
        autoTrim_ = autoTrim;
    }

    /**
     * ���ׂẴZ�N�V���������擾���܂��B
     * 
     * @return �Z�N�V�������̃��X�g
     */
    public List<String> getSectionNames() {
        List<String> result = new ArrayList<String>();
        for (Section s : ini_.getSections()) {
            result.add(s.getName());
        }
        return result;
    }

    /**
     * �w�肳�ꂽ�Z�N�V�����̎����ׂẴp�����[�^���A�l�ƃL�[�̃}�b�v�Ƃ��Ď擾���܂��B
     * �Z�N�V���������݂��Ȃ��ꍇ��{@code null}��Ԃ��܂��B
     * �p�����[�^�������̒l�����ꍇ�͐擪�̒l�݂̂����擾�ł��܂���B
     * 
     * @param section  �Z�N�V����
     * @return �l�ƃL�[�̃}�b�v
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
     * �w�肳�ꂽ�Z�N�V�����̎����ׂẴp�����[�^�̃L�[���擾���܂��B
     * �Z�N�V���������݂��Ȃ��ꍇ��{@code null}��Ԃ��܂��B
     * 
     * @param section  �Z�N�V����
     * @return �L�[�̃��X�g
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
     * �w�肳�ꂽ�Z�N�V�����A�L�[�̃p�����[�^�������̒l���������肵�܂��B
     * �p�����[�^�����݂��Ȃ��ꍇ��{@code false}��Ԃ��܂��B
     * 
     * @param section  �Z�N�V����
     * @param key      �L�[
     * @return �����̒l�����ꍇ�� {@code true}
     */
    public boolean hasMultiValues(String section, String key) {
        Parameter p = getParameter(section, key);
        if (p == null) {
            return false;
        }
        return p.hasMultiValues();
    }

    /**
     * �w�肳�ꂽ�Z�N�V�����A�L�[�̒l���擾���܂��B
     * �p�����[�^�������̒l�����ꍇ�͐擪�̒l���擾���܂��B
     * �p�����[�^�����݂��Ȃ��ꍇ��{@code null}��Ԃ��܂��B
     * 
     * @param section  �Z�N�V����
     * @param key      �L�[
     * @return �l
     */
    public String get(String section, String key) {
        return get(section, key, null);
    }

    /**
     * �w�肳�ꂽ�Z�N�V�����A�L�[�̒l���擾���܂��B
     * �p�����[�^�������̒l�����ꍇ�͐擪�̒l���擾���܂��B
     * �p�����[�^�����݂��Ȃ��ꍇ�͎w�肳�ꂽ�f�t�H���g�l��Ԃ��܂��B
     * 
     * @param section   �Z�N�V����
     * @param key       �L�[
     * @param defValue  �f�t�H���g�l
     * @return �l
     */
    public String get(String section, String key, String defValue) {
        Parameter p = getParameter(section, key);
        if (p == null) {
            return defValue;
        }
        return p == null ? defValue : p.getValue();
    }

    /**
     * �w�肳�ꂽ�Z�N�V�����A�L�[�̃p�����[�^�������ׂĂ̒l���擾���܂��B
     * 
     * @param section  �Z�N�V����
     * @param key      �L�[
     * @return �l�̃��X�g
     */
    public List<String> getMulti(String section, String key) {
        Parameter p = getParameter(section, key);
        if (p == null) {
            return new ArrayList<String>();
        }
        return p.getValues();
    }

    /**
     * �w�肳�ꂽ�Z�N�V�����A�L�[�̒l��boolean�^�ɕϊ����Ď擾���܂��B
     * �p�����[�^�������̒l�����ꍇ�͐擪�̒l���擾���܂��B
     * �p�����[�^�����݂��Ȃ��ꍇ��{@code false}��Ԃ��܂��B
     * 
     * @param section  �Z�N�V����
     * @param key      �L�[
     * @return boolean�^�̒l
     */
    public boolean getBoolean(String section, String key) {
        String value = get(section, key);
        if (value != null && autoTrim_) {
            value = value.trim();
        }
        return Boolean.parseBoolean(value);
    }

    /**
     * �w�肳�ꂽ�Z�N�V�����A�L�[�̃p�����[�^�������ׂĂ̒l��boolean�^�ɕϊ����Ď擾���܂��B
     * 
     * @param section  �Z�N�V����
     * @param key      �L�[
     * @return �l�̃��X�g
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
     * �w�肳�ꂽ�Z�N�V�����A�L�[�̒l��byte�^�ɕϊ����Ď擾���܂��B
     * �p�����[�^�������̒l�����ꍇ�͐擪�̒l���擾���܂��B
     * �p�����[�^�����݂��Ȃ��A�܂���byte�^�ɕϊ��ł��Ȃ��ꍇ��0��Ԃ��܂��B
     * 
     * @param section  �Z�N�V����
     * @param key      �L�[
     * @return byte�^�̒l
     */
    public byte getByte(String section, String key) {
        return getByte(section, key, (byte) 0);
    }

    /**
     * �w�肳�ꂽ�Z�N�V�����A�L�[�̒l��byte�^�ɕϊ����Ď擾���܂��B
     * �p�����[�^�������̒l�����ꍇ�͐擪�̒l���擾���܂��B
     * �p�����[�^�����݂��Ȃ��A�܂���byte�^�ɕϊ��ł��Ȃ��ꍇ�͎w�肳�ꂽ�f�t�H���g�l��Ԃ��܂��B
     * 
     * @param section   �Z�N�V����
     * @param key       �L�[
     * @param defValue  �f�t�H���g�l
     * @return byte�^�̒l
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
     * �w�肳�ꂽ�Z�N�V�����A�L�[�̃p�����[�^�������ׂĂ̒l��byte�^�ɕϊ����Ď擾���܂��B
     * byte�^�ɕϊ��ł��Ȃ��ꍇ�͎w�肳�ꂽ�f�t�H���g�l��Ԃ��܂��B
     * 
     * @param section   �Z�N�V����
     * @param key       �L�[
     * @param defValue  �f�t�H���g�l
     * @return �l�̃��X�g
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
     * �w�肳�ꂽ�Z�N�V�����A�L�[�̒l��short�^�ɕϊ����Ď擾���܂��B
     * �p�����[�^�������̒l�����ꍇ�͐擪�̒l���擾���܂��B
     * �p�����[�^�����݂��Ȃ��A�܂���short�^�ɕϊ��ł��Ȃ��ꍇ��0��Ԃ��܂��B
     * 
     * @param section  �Z�N�V����
     * @param key      �L�[
     * @return short�^�̒l
     */
    public short getShort(String section, String key) {
        return getShort(section, key, (short) 0);
    }

    /**
     * �w�肳�ꂽ�Z�N�V�����A�L�[�̒l��short�^�ɕϊ����Ď擾���܂��B
     * �p�����[�^�������̒l�����ꍇ�͐擪�̒l���擾���܂��B
     * �p�����[�^�����݂��Ȃ��A�܂���short�^�ɕϊ��ł��Ȃ��ꍇ�͎w�肳�ꂽ�f�t�H���g�l��Ԃ��܂��B
     * 
     * @param section   �Z�N�V����
     * @param key       �L�[
     * @param defValue  �f�t�H���g�l
     * @return short�^�̒l
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
     * �w�肳�ꂽ�Z�N�V�����A�L�[�̃p�����[�^�������ׂĂ̒l��short�^�ɕϊ����Ď擾���܂��B
     * short�^�ɕϊ��ł��Ȃ��ꍇ�͎w�肳�ꂽ�f�t�H���g�l��Ԃ��܂��B
     * 
     * @param section   �Z�N�V����
     * @param key       �L�[
     * @param defValue  �f�t�H���g�l
     * @return �l�̃��X�g
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
     * �w�肳�ꂽ�Z�N�V�����A�L�[�̒l��int�^�ɕϊ����Ď擾���܂��B
     * �p�����[�^�������̒l�����ꍇ�͐擪�̒l���擾���܂��B
     * �p�����[�^�����݂��Ȃ��A�܂���int�^�ɕϊ��ł��Ȃ��ꍇ��0��Ԃ��܂��B
     * 
     * @param section  �Z�N�V����
     * @param key      �L�[
     * @return int�^�̒l
     */
    public int getInt(String section, String key) {
        return getInt(section, key, 0);
    }

    /**
     * �w�肳�ꂽ�Z�N�V�����A�L�[�̒l��int�^�ɕϊ����Ď擾���܂��B
     * �p�����[�^�������̒l�����ꍇ�͐擪�̒l���擾���܂��B
     * �p�����[�^�����݂��Ȃ��A�܂���int�^�ɕϊ��ł��Ȃ��ꍇ�͎w�肳�ꂽ�f�t�H���g�l��Ԃ��܂��B
     * 
     * @param section   �Z�N�V����
     * @param key       �L�[
     * @param defValue  �f�t�H���g�l
     * @return int�^�̒l
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
     * �w�肳�ꂽ�Z�N�V�����A�L�[�̃p�����[�^�������ׂĂ̒l��int�^�ɕϊ����Ď擾���܂��B
     * int�^�ɕϊ��ł��Ȃ��ꍇ�͎w�肳�ꂽ�f�t�H���g�l��Ԃ��܂��B
     * 
     * @param section   �Z�N�V����
     * @param key       �L�[
     * @param defValue  �f�t�H���g�l
     * @return �l�̃��X�g
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
     * �w�肳�ꂽ�Z�N�V�����A�L�[�̒l��long�^�ɕϊ����Ď擾���܂��B
     * �p�����[�^�������̒l�����ꍇ�͐擪�̒l���擾���܂��B
     * �p�����[�^�����݂��Ȃ��A�܂���long�^�ɕϊ��ł��Ȃ��ꍇ��0��Ԃ��܂��B
     * 
     * @param section  �Z�N�V����
     * @param key      �L�[
     * @return long�^�̒l
     */
    public long getLong(String section, String key) {
        return getLong(section, key, 0L);
    }

    /**
     * �w�肳�ꂽ�Z�N�V�����A�L�[�̒l��long�^�ɕϊ����Ď擾���܂��B
     * �p�����[�^�������̒l�����ꍇ�͐擪�̒l���擾���܂��B
     * �p�����[�^�����݂��Ȃ��A�܂���long�^�ɕϊ��ł��Ȃ��ꍇ�͎w�肳�ꂽ�f�t�H���g�l��Ԃ��܂��B
     * 
     * @param section   �Z�N�V����
     * @param key       �L�[
     * @param defValue  �f�t�H���g�l
     * @return long�^�̒l
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
     * �w�肳�ꂽ�Z�N�V�����A�L�[�̃p�����[�^�������ׂĂ̒l��long�^�ɕϊ����Ď擾���܂��B
     * long�^�ɕϊ��ł��Ȃ��ꍇ�͎w�肳�ꂽ�f�t�H���g�l��Ԃ��܂��B
     * 
     * @param section   �Z�N�V����
     * @param key       �L�[
     * @param defValue  �f�t�H���g�l
     * @return �l�̃��X�g
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
     * �w�肳�ꂽ�Z�N�V�����A�L�[�̒l��float�^�ɕϊ����Ď擾���܂��B
     * �p�����[�^�������̒l�����ꍇ�͐擪�̒l���擾���܂��B
     * �p�����[�^�����݂��Ȃ��A�܂���float�^�ɕϊ��ł��Ȃ��ꍇ��0.0��Ԃ��܂��B
     * 
     * @param section  �Z�N�V����
     * @param key      �L�[
     * @return float�^�̒l
     */
    public float getFloat(String section, String key) {
        return getFloat(section, key, 0.0F);
    }

    /**
     * �w�肳�ꂽ�Z�N�V�����A�L�[�̒l��float�^�ɕϊ����Ď擾���܂��B
     * �p�����[�^�������̒l�����ꍇ�͐擪�̒l���擾���܂��B
     * �p�����[�^�����݂��Ȃ��A�܂���float�^�ɕϊ��ł��Ȃ��ꍇ�͎w�肳�ꂽ�f�t�H���g�l��Ԃ��܂��B
     * 
     * @param section   �Z�N�V����
     * @param key       �L�[
     * @param defValue  �f�t�H���g�l
     * @return float�^�̒l
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
     * �w�肳�ꂽ�Z�N�V�����A�L�[�̃p�����[�^�������ׂĂ̒l��float�^�ɕϊ����Ď擾���܂��B
     * float�^�ɕϊ��ł��Ȃ��ꍇ�͎w�肳�ꂽ�f�t�H���g�l��Ԃ��܂��B
     * 
     * @param section   �Z�N�V����
     * @param key       �L�[
     * @param defValue  �f�t�H���g�l
     * @return �l�̃��X�g
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
     * �w�肳�ꂽ�Z�N�V�����A�L�[�̒l��double�^�ɕϊ����Ď擾���܂��B
     * �p�����[�^�������̒l�����ꍇ�͐擪�̒l���擾���܂��B
     * �p�����[�^�����݂��Ȃ��A�܂���double�^�ɕϊ��ł��Ȃ��ꍇ��0.0��Ԃ��܂��B
     * 
     * @param section  �Z�N�V����
     * @param key      �L�[
     * @return double�^�̒l
     */
    public double getDouble(String section, String key) {
        return getDouble(section, key, 0.0F);
    }

    /**
     * �w�肳�ꂽ�Z�N�V�����A�L�[�̒l��double�^�ɕϊ����Ď擾���܂��B
     * �p�����[�^�������̒l�����ꍇ�͐擪�̒l���擾���܂��B
     * �p�����[�^�����݂��Ȃ��A�܂���double�^�ɕϊ��ł��Ȃ��ꍇ�͎w�肳�ꂽ�f�t�H���g�l��Ԃ��܂��B
     * 
     * @param section   �Z�N�V����
     * @param key       �L�[
     * @param defValue  �f�t�H���g�l
     * @return double�^�̒l
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
     * �w�肳�ꂽ�Z�N�V�����A�L�[�̃p�����[�^�������ׂĂ̒l��double�^�ɕϊ����Ď擾���܂��B
     * double�^�ɕϊ��ł��Ȃ��ꍇ�͎w�肳�ꂽ�f�t�H���g�l��Ԃ��܂��B
     * 
     * @param section   �Z�N�V����
     * @param key       �L�[
     * @param defValue  �f�t�H���g�l
     * @return �l�̃��X�g
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