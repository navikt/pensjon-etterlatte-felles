import { StepProps } from '../Dialogue'
import Navigation from '../../common/Navigation'
import { FormProvider, useForm } from 'react-hook-form'
import { IDeceasedParent } from '../../../context/application/application'
import { useApplicationContext } from '../../../context/application/ApplicationContext'
import useTranslation from '../../../hooks/useTranslation'
import ErrorSummaryWrapper from '../../common/ErrorSummaryWrapper'
import DeceasedParentForm from '../the-deceased/DeceasedParentForm'
import DeceasedParentTitle from '../the-deceased/DeceasedParentTitle'

export default function DeceasedParent({ next, prev, type }: StepProps) {
    const { state, dispatch } = useApplicationContext()
    const { t } = useTranslation('navigation')

    const save = (data: IDeceasedParent) => {
        dispatch({ type: type!!, payload: { ...data } })
        next!!()
    }

    const methods = useForm<any>({
        defaultValues: { ...state.secondParent } || {},
        shouldUnregister: true,
    })

    const {
        handleSubmit,
        formState: { errors },
    } = methods

    return (
        <FormProvider {...methods}>
            <form>
                <DeceasedParentTitle type={type!!} situation={state?.applicant?.applicantSituation} />

                <DeceasedParentForm />

                <ErrorSummaryWrapper errors={errors} />

                <Navigation
                    right={{ label: t('saveButton'), onClick: handleSubmit(save) }}
                    left={{ label: t('backButton'), variant: 'secondary', onClick: prev }}
                    hideCancel={true}
                />
            </form>
        </FormProvider>
    )
}