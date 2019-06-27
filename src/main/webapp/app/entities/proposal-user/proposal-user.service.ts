import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IProposalUser } from 'app/shared/model/proposal-user.model';

type EntityResponseType = HttpResponse<IProposalUser>;
type EntityArrayResponseType = HttpResponse<IProposalUser[]>;

@Injectable({ providedIn: 'root' })
export class ProposalUserService {
  public resourceUrl = SERVER_API_URL + 'api/proposal-users';

  constructor(protected http: HttpClient) {}

  create(proposalUser: IProposalUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(proposalUser);
    return this.http
      .post<IProposalUser>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(proposalUser: IProposalUser): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(proposalUser);
    return this.http
      .put<IProposalUser>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IProposalUser>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProposalUser[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(proposalUser: IProposalUser): IProposalUser {
    const copy: IProposalUser = Object.assign({}, proposalUser, {
      creationDate: proposalUser.creationDate != null && proposalUser.creationDate.isValid() ? proposalUser.creationDate.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.creationDate = res.body.creationDate != null ? moment(res.body.creationDate) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((proposalUser: IProposalUser) => {
        proposalUser.creationDate = proposalUser.creationDate != null ? moment(proposalUser.creationDate) : null;
      });
    }
    return res;
  }
}
