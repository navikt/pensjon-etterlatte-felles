import { Close, InformationFilled } from '@navikt/ds-icons'
import { Button } from '@navikt/ds-react'
import cl from 'classnames'
import React, { forwardRef } from 'react'
import styled from 'styled-components'
import useTranslation from '../../hooks/useTranslation'

const LogOutAlertButton = styled(Button)`
    position: absolute;
    top: 0.75rem;
    right: 0.75rem;
    display: flex;
    padding: 0.5rem;

    svg {
        height: 1.5rem;
        width: 1.5rem;
    }
`

export interface AlertProps extends React.HTMLAttributes<HTMLDivElement> {
    children: React.ReactNode
    onClose: () => void
}

const Alert = forwardRef<HTMLDivElement, AlertProps>(({ children, onClose, className, ...rest }, ref) => {
    const { t } = useTranslation('logOutUser')

    return (
        <div
            {...rest}
            ref={ref}
            className={cl(className, 'navds-alert', `navds-alert--warning`, `navds-alert--medium`)}
        >
            <LogOutAlertButton size="small" variant="tertiary" aria-label="lukk melding" onClick={onClose}>
                <Close title={t('btn')} />
            </LogOutAlertButton>
            <InformationFilled title={`warning-ikon`} className="navds-alert__icon" />
            <div className="navds-alert__wrapper">{children}</div>
        </div>
    )
})

export default Alert