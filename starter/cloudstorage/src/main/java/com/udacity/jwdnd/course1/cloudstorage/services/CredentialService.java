package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CredentialService {

    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    @Autowired
    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public List<Credential> getCredentialsByUserId(Integer userId) {
        return credentialMapper.findByUserId(userId);
    }

    public int addCredential(Credential credential) {
        getObjectWithHashedValue(credential);
        return credentialMapper.addCredential(credential);
    }

    public int updateCredential(Credential credential) {
        getObjectWithHashedValue(credential);
        return credentialMapper.updateCredential(credential);
    }

    public int deleteCredential(int credentialId) {
        return credentialMapper.deleteCredential(credentialId);
    }

    private void getObjectWithHashedValue(Credential credential) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);
        credential.setKey(encodedKey);
        credential.setPassword(encryptedPassword);
    }

    public List<String> getDecryptedPasswords(Integer userId) {
        List<Credential> credentialList = getCredentialsByUserId(userId);

        return credentialList != null ? credentialList.stream()
                .filter(Objects::nonNull)
                .map(credential -> encryptionService.decryptValue(credential.getPassword(), credential.getKey()))
                .collect(Collectors.toList()) : new ArrayList<>();
    }

    public boolean isUniqueUserName(Integer userId, String userName) {
        Optional<Credential> credential = Optional.ofNullable(credentialMapper.uniqueUserName(userId, userName));
        return (credential.isEmpty());
    }
}
