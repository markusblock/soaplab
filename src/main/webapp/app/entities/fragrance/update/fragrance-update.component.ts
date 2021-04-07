import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IFragrance, Fragrance } from '../fragrance.model';
import { FragranceService } from '../service/fragrance.service';

@Component({
  selector: 'jhi-fragrance-update',
  templateUrl: './fragrance-update.component.html',
})
export class FragranceUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    inci: [],
    typ: [],
  });

  constructor(protected fragranceService: FragranceService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ fragrance }) => {
      this.updateForm(fragrance);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const fragrance = this.createFromForm();
    if (fragrance.id !== undefined) {
      this.subscribeToSaveResponse(this.fragranceService.update(fragrance));
    } else {
      this.subscribeToSaveResponse(this.fragranceService.create(fragrance));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFragrance>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(fragrance: IFragrance): void {
    this.editForm.patchValue({
      id: fragrance.id,
      name: fragrance.name,
      inci: fragrance.inci,
      typ: fragrance.typ,
    });
  }

  protected createFromForm(): IFragrance {
    return {
      ...new Fragrance(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      inci: this.editForm.get(['inci'])!.value,
      typ: this.editForm.get(['typ'])!.value,
    };
  }
}
