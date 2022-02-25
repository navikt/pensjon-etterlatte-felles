import { Controller, FieldError, FieldPath, FieldValues, useFormContext } from 'react-hook-form'
import { CheckboksPanel, Checkbox, CheckboxGruppe, SkjemaGruppeProps } from 'nav-frontend-skjema'
import { CheckboxProps } from 'nav-frontend-skjema/lib/checkbox'
import { get } from 'lodash'
import useTranslation from '../../../hooks/useTranslation'
import { getErrorKey } from '../../../utils/errors'

const handleSelect = (array: any[], addOrRemove: any) => {
    return array?.includes(addOrRemove)
        ? array?.filter((value) => value !== addOrRemove)
        : [...(array ?? []), addOrRemove]
}

interface RHFCheckboksPanelGruppeProps extends Omit<SkjemaGruppeProps, 'onChange' | 'children'> {
    name: FieldPath<FieldValues>
    checkboxes: CheckboxProps[]
}

export function RHFCheckboksPanelGruppe({ name, checkboxes, ...rest }: RHFCheckboksPanelGruppeProps) {
    const { t } = useTranslation('error')

    const {
        control,
        formState: { errors },
    } = useFormContext()

    const error: FieldError = get(errors, name)
    const feilmelding = !!error ? t(getErrorKey(error)) : undefined

    return (
        <div id={name}>
            <Controller
                name={name}
                control={control}
                rules={{ required: true }}
                render={({ field: { value, onChange } }) => (
                    <CheckboxGruppe {...rest} feil={feilmelding} className={'inputPanelGruppe'}>
                        {checkboxes.map((checkbox: CheckboxProps) => (
                            <CheckboksPanel
                                key={checkbox.value as string}
                                label={checkbox.label}
                                value={checkbox.value}
                                required={checkbox.required}
                                checked={(value as any[])?.includes(checkbox.value)}
                                onChange={(e) =>
                                    onChange(handleSelect(value, (e.target as HTMLInputElement).value as any))
                                }
                            />
                        ))}
                    </CheckboxGruppe>
                )}
            />
        </div>
    )
}

interface RHFCheckboksPanelProps extends Omit<SkjemaGruppeProps, 'onChange' | 'children'> {
    name: FieldPath<FieldValues>
    checkbox: CheckboxProps
    valgfri?: boolean
}

export function RHFCheckboksPanel({ name, checkbox, valgfri, ...rest }: RHFCheckboksPanelProps) {
    const { control } = useFormContext()

    return (
        <div id={name}>
            <Controller
                name={name}
                control={control}
                rules={{ required: !valgfri }}
                render={({ field: { value, onChange } }) => (
                    <CheckboxGruppe {...rest} className={'inputPanelGruppe'}>
                        <CheckboksPanel
                            key={checkbox.value as string}
                            label={checkbox.label}
                            value={checkbox.value}
                            checked={value}
                            onChange={(e) => {
                                e.target.checked ? onChange(e.target.value) : onChange(null)
                            }}
                        />
                    </CheckboxGruppe>
                )}
            />
        </div>
    )
}

interface RHFCheckboksGruppeProps extends Omit<SkjemaGruppeProps, 'onChange' | 'children'> {
    name: FieldPath<FieldValues>
    checkboxes: CheckboxProps[]
}

export function RHFCheckboksGruppe({ name, checkboxes, ...rest }: RHFCheckboksGruppeProps) {
    const { t } = useTranslation('error')

    const {
        control,
        formState: { errors },
    } = useFormContext()

    const error: FieldError = get(errors, name)
    const feilmelding = !!error ? t(getErrorKey(error)) : undefined

    return (
        <div id={name}>
            <Controller
                name={name}
                control={control}
                rules={{ required: true }}
                render={({ field: { value, onChange } }) => (
                    <CheckboxGruppe {...rest} feil={feilmelding}>
                        {checkboxes.map((checkbox: CheckboxProps) => (
                            <Checkbox
                                key={checkbox.value as string}
                                label={checkbox.label}
                                value={checkbox.value}
                                required={checkbox.required}
                                checked={(value as any[])?.includes(checkbox.value)}
                                onChange={(e) =>
                                    onChange(handleSelect(value, (e.target as HTMLInputElement).value as any))
                                }
                            />
                        ))}
                    </CheckboxGruppe>
                )}
            />
        </div>
    )
}