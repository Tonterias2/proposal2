import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IVoteProposal } from 'app/shared/model/vote-proposal.model';

type EntityResponseType = HttpResponse<IVoteProposal>;
type EntityArrayResponseType = HttpResponse<IVoteProposal[]>;

@Injectable({ providedIn: 'root' })
export class VoteProposalService {
  public resourceUrl = SERVER_API_URL + 'api/vote-proposals';

  constructor(protected http: HttpClient) {}

  create(voteProposal: IVoteProposal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(voteProposal);
    return this.http
      .post<IVoteProposal>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(voteProposal: IVoteProposal): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(voteProposal);
    return this.http
      .put<IVoteProposal>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IVoteProposal>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVoteProposal[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(voteProposal: IVoteProposal): IVoteProposal {
    const copy: IVoteProposal = Object.assign({}, voteProposal, {
      creationDate: voteProposal.creationDate != null && voteProposal.creationDate.isValid() ? voteProposal.creationDate.toJSON() : null
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
      res.body.forEach((voteProposal: IVoteProposal) => {
        voteProposal.creationDate = voteProposal.creationDate != null ? moment(voteProposal.creationDate) : null;
      });
    }
    return res;
  }
}
