import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { IPost, Post } from 'app/shared/model/post.model';
import { PostService } from './post.service';

@Component({
  selector: 'jhi-post-update',
  templateUrl: './post-update.component.html'
})
export class PostUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    creationDate: [null, [Validators.required]],
    postName: [null, [Validators.required, Validators.minLength(2), Validators.maxLength(250)]]
  });

  constructor(protected postService: PostService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ post }) => {
      this.updateForm(post);
    });
  }

  updateForm(post: IPost) {
    const date = moment(moment().format('YYYY-MM-DDTHH:mm'), 'YYYY-MM-DDTHH:mm');
    this.editForm.patchValue({
      id: post.id,
      //      creationDate: post.creationDate != null ? post.creationDate.format(DATE_TIME_FORMAT) : null,
      creationDate:
        post.creationDate != null
          ? post.creationDate.format(DATE_TIME_FORMAT)
          : JSON.stringify(date)
              .split(':00.000Z')
              .join('')
              .split('"')
              .join(''),
      postName: post.postName
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const post = this.createFromForm();
    if (post.id !== undefined) {
      this.subscribeToSaveResponse(this.postService.update(post));
    } else {
      this.subscribeToSaveResponse(this.postService.create(post));
    }
  }

  private createFromForm(): IPost {
    return {
      ...new Post(),
      id: this.editForm.get(['id']).value,
      creationDate:
        this.editForm.get(['creationDate']).value != null ? moment(this.editForm.get(['creationDate']).value, DATE_TIME_FORMAT) : undefined,
      postName: this.editForm.get(['postName']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPost>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
