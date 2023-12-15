package io.security.corespringsecurity.security.voter;

import io.security.corespringsecurity.repository.AccessIpRepository;
import io.security.corespringsecurity.service.SecurityResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.util.Collection;
import java.util.List;

public class IpAddressVoter implements AccessDecisionVoter<Object> {
    private SecurityResourceService securityResourceService;

    public IpAddressVoter(final SecurityResourceService securityResourceService) {
        this.securityResourceService = securityResourceService;
    }

    @Override
    public boolean supports(final ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(final Class<?> clazz) {
        return true;
    }

    @Override
    public int vote(final Authentication authentication, final Object object, final Collection<ConfigAttribute> attributes) {
        final WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
        final String remoteAddress = details.getRemoteAddress();

        final List<String> accessIpList = securityResourceService.getAccessIpList();

        for (String ipAddress : accessIpList) {
            if (remoteAddress.equals(ipAddress)) {
                return ACCESS_ABSTAIN;
            }
        }

        throw new AccessDeniedException("Invalid IpAddress");
    }
}
